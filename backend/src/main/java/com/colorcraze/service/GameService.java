package com.colorcraze.service;

import com.colorcraze.dto.*;
import com.colorcraze.model.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final Map<String, Game> activeGames = new ConcurrentHashMap<>();
    private final Map<String, Thread> gameThreads = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final Random random = new Random();
    
    private static final double MOVE_SPEED = 0.3;
    private static final double JUMP_VELOCITY = -1.2;
    private static final double GRAVITY = 0.08;
    private static final int TICK_RATE_MS = 33; // ~30 FPS
    
    public GameService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    public CreateGameResponse createGame(String playerName) {
        String code = generateGameCode();
        Game game = new Game(code);
        
        String playerId = UUID.randomUUID().toString();
        Player player = new Player(playerId, playerName, PlayerColor.RED);
        game.addPlayer(player);
        
        activeGames.put(code, game);
        startLobbyTimer(code);
        
        return new CreateGameResponse(code, playerId);
    }
    
    public JoinGameResponse joinGame(String code, String playerName, PlayerColor color) {
        Game game = activeGames.get(code);
        if (game == null) {
            return new JoinGameResponse(null, false, "Game not found");
        }
        
        if (game.getState() != Game.GameState.LOBBY) {
            return new JoinGameResponse(null, false, "Game already started");
        }
        
        if (game.getPlayers().size() >= Game.getMaxPlayers()) {
            return new JoinGameResponse(null, false, "Game is full");
        }
        
        if (isColorTaken(game, color)) {
            return new JoinGameResponse(null, false, "Color already taken");
        }
        
        String playerId = UUID.randomUUID().toString();
        Player player = new Player(playerId, playerName, color);
        game.addPlayer(player);
        
        broadcastGameState(code);
        
        return new JoinGameResponse(playerId, true, "Joined successfully");
    }
    
    private boolean isColorTaken(Game game, PlayerColor color) {
        return game.getPlayers().values().stream()
                .anyMatch(p -> p.getColor() == color);
    }
    
    public GameStateDTO getGameState(String code) {
        Game game = activeGames.get(code);
        if (game == null) {
            return null;
        }
        return buildGameStateDTO(game);
    }
    
    public void handleMove(String code, MoveRequest moveRequest) {
        Game game = activeGames.get(code);
        if (game == null || game.getState() != Game.GameState.PLAYING) {
            return;
        }
        
        Player player = game.getPlayers().get(moveRequest.getPlayerId());
        if (player == null) {
            return;
        }
        
        String direction = moveRequest.getDirection();
        if ("left".equals(direction)) {
            player.setVelocityX(-MOVE_SPEED);
        } else if ("right".equals(direction)) {
            player.setVelocityX(MOVE_SPEED);
        } else if ("stop".equals(direction)) {
            player.setVelocityX(0);
        }
        
        if (moveRequest.isJump() && !player.isJumping()) {
            player.setVelocityY(JUMP_VELOCITY);
            player.setJumping(true);
        }
    }
    
    private void startLobbyTimer(String code) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            Game game = activeGames.get(code);
            if (game != null && game.getState() == Game.GameState.LOBBY) {
                startGame(code);
            }
            scheduler.shutdown();
        }, Game.getLobbyDurationSeconds(), TimeUnit.SECONDS);
    }
    
    private void startGame(String code) {
        Game game = activeGames.get(code);
        if (game == null) {
            return;
        }
        
        game.startGame();
        broadcastGameState(code);
        
        Thread gameThread = new Thread(() -> runGameLoop(code));
        gameThread.start();
        gameThreads.put(code, gameThread);
    }
    
    private void runGameLoop(String code) {
        Game game = activeGames.get(code);
        if (game == null) {
            return;
        }
        
        while (game.isRunning() && !game.isGameExpired()) {
            try {
                updateGameState(game);
                broadcastGameState(code);
                Thread.sleep(TICK_RATE_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        endGame(code);
    }
    
    private void updateGameState(Game game) {
        for (Player player : game.getPlayers().values()) {
            updatePlayerPhysics(player, game.getBoard());
            paintCellsUnderPlayer(player, game.getBoard());
        }
    }
    
    private void updatePlayerPhysics(Player player, Board board) {
        player.setX(player.getX() + player.getVelocityX());
        player.setY(player.getY() + player.getVelocityY());
        
        if (player.isJumping()) {
            player.setVelocityY(player.getVelocityY() + GRAVITY);
        }
        
        if (player.getX() < 0) player.setX(0);
        if (player.getX() >= board.getWidth()) player.setX(board.getWidth() - 1);
        
        int groundY = board.getHeight() - 2;
        if (player.getY() >= groundY) {
            player.setY(groundY);
            player.setVelocityY(0);
            player.setJumping(false);
        }
        
        if (player.getY() < 0) {
            player.setY(0);
            player.setVelocityY(0);
        }
    }
    
    private void paintCellsUnderPlayer(Player player, Board board) {
        int cellX = (int) player.getX();
        int cellY = (int) player.getY();
        
        board.paintCell(cellX, cellY, player.getColor());
        board.paintCell(cellX, cellY + 1, player.getColor());
    }
    
    private void endGame(String code) {
        Game game = activeGames.get(code);
        if (game == null) {
            return;
        }
        
        game.finishGame();
        
        GameResultDTO resultDTO = buildGameResultDTO(game);
        messagingTemplate.convertAndSend("/topic/games/" + code + "/results", resultDTO);
        
        gameThreads.remove(code);
        
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            activeGames.remove(code);
            scheduler.shutdown();
        }, 60, TimeUnit.SECONDS);
    }
    
    private void broadcastGameState(String code) {
        GameStateDTO stateDTO = getGameState(code);
        if (stateDTO != null) {
            messagingTemplate.convertAndSend("/topic/games/" + code + "/state", stateDTO);
        }
    }
    
    private GameStateDTO buildGameStateDTO(Game game) {
        GameStateDTO dto = new GameStateDTO();
        dto.setGameCode(game.getCode());
        dto.setState(game.getState().name());
        
        List<GameStateDTO.PlayerDTO> playerDTOs = game.getPlayers().values().stream()
                .map(GameStateDTO.PlayerDTO::new)
                .collect(Collectors.toList());
        dto.setPlayers(playerDTOs);
        
        Map<String, String> boardData = new HashMap<>();
        Board board = game.getBoard();
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                PlayerColor color = board.getCellColor(x, y);
                if (color != null) {
                    boardData.put(x + "," + y, color.name());
                }
            }
        }
        dto.setBoard(boardData);
        
        if (game.getState() == Game.GameState.LOBBY) {
            dto.setRemainingTime(game.getRemainingLobbyTime());
        } else if (game.getState() == Game.GameState.PLAYING) {
            dto.setRemainingTime(game.getRemainingGameTime());
        }
        
        return dto;
    }
    
    private GameResultDTO buildGameResultDTO(Game game) {
        GameResultDTO dto = new GameResultDTO();
        
        Player winner = game.getWinner();
        if (winner != null) {
            dto.setWinnerId(winner.getId());
            dto.setWinnerName(winner.getName());
            dto.setWinnerColor(winner.getColor().name());
        }
        
        List<GameResultDTO.PlayerScoreDTO> scores = game.getPlayers().values().stream()
                .map(p -> new GameResultDTO.PlayerScoreDTO(
                        p.getId(), p.getName(), p.getColor().name(), p.getScore()))
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());
        dto.setScores(scores);
        
        return dto;
    }
    
    private String generateGameCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }
}
