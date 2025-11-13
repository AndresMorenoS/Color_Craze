package com.colorcraze.model;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    public enum GameState {
        LOBBY,
        PLAYING,
        FINISHED
    }
    
    private final String code;
    private final Map<String, Player> players;
    private final Board board;
    private GameState state;
    private Instant lobbyStartTime;
    private Instant gameStartTime;
    private Instant gameEndTime;
    private volatile boolean running;
    
    private static final int LOBBY_DURATION_SECONDS = 15;
    private static final int GAME_DURATION_SECONDS = 30;
    private static final int BOARD_WIDTH = 40;
    private static final int BOARD_HEIGHT = 30;
    private static final int MAX_PLAYERS = 4;
    
    public Game(String code) {
        this.code = code;
        this.players = new ConcurrentHashMap<>();
        this.board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        this.state = GameState.LOBBY;
        this.lobbyStartTime = Instant.now();
        this.running = false;
    }
    
    public boolean addPlayer(Player player) {
        if (players.size() >= MAX_PLAYERS || state != GameState.LOBBY) {
            return false;
        }
        players.put(player.getId(), player);
        return true;
    }
    
    public void removePlayer(String playerId) {
        players.remove(playerId);
    }
    
    public void startGame() {
        this.state = GameState.PLAYING;
        this.gameStartTime = Instant.now();
        this.gameEndTime = gameStartTime.plusSeconds(GAME_DURATION_SECONDS);
        this.running = true;
        initializePlayerPositions();
    }
    
    private void initializePlayerPositions() {
        List<Player> playerList = new ArrayList<>(players.values());
        int spacing = BOARD_WIDTH / (playerList.size() + 1);
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            player.setX((i + 1) * spacing);
            player.setY(BOARD_HEIGHT - 2); // Player stands on top of the floor block (which is at y=29)
        }
    }
    
    public void finishGame() {
        this.state = GameState.FINISHED;
        this.running = false;
        calculateFinalScores();
    }
    
    private void calculateFinalScores() {
        Map<PlayerColor, Integer> colorScores = board.calculateScores();
        for (Player player : players.values()) {
            player.setScore(colorScores.getOrDefault(player.getColor(), 0));
        }
    }
    
    public boolean isLobbyExpired() {
        return Instant.now().isAfter(lobbyStartTime.plusSeconds(LOBBY_DURATION_SECONDS));
    }
    
    public boolean isGameExpired() {
        return gameEndTime != null && Instant.now().isAfter(gameEndTime);
    }
    
    public long getRemainingLobbyTime() {
        long elapsed = Instant.now().getEpochSecond() - lobbyStartTime.getEpochSecond();
        return Math.max(0, LOBBY_DURATION_SECONDS - elapsed);
    }
    
    public long getRemainingGameTime() {
        if (gameEndTime == null) return 0;
        long remaining = gameEndTime.getEpochSecond() - Instant.now().getEpochSecond();
        return Math.max(0, remaining);
    }
    
    public Player getWinner() {
        return players.values().stream()
                .max(Comparator.comparingInt(Player::getScore))
                .orElse(null);
    }
    
    // Getters
    public String getCode() { return code; }
    public Map<String, Player> getPlayers() { return players; }
    public Board getBoard() { return board; }
    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }
    public boolean isRunning() { return running; }
    public void setRunning(boolean running) { this.running = running; }
    
    public static int getLobbyDurationSeconds() { return LOBBY_DURATION_SECONDS; }
    public static int getGameDurationSeconds() { return GAME_DURATION_SECONDS; }
    public static int getMaxPlayers() { return MAX_PLAYERS; }
}
