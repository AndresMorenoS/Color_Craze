package com.colorcraze.dto;

import com.colorcraze.model.Player;
import com.colorcraze.model.PlayerColor;

import java.util.List;
import java.util.Map;

public class GameStateDTO {
    private String gameCode;
    private String state;
    private List<PlayerDTO> players;
    private Map<String, String> board;
    private long remainingTime;
    
    public GameStateDTO() {}
    
    public String getGameCode() { return gameCode; }
    public void setGameCode(String gameCode) { this.gameCode = gameCode; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public List<PlayerDTO> getPlayers() { return players; }
    public void setPlayers(List<PlayerDTO> players) { this.players = players; }
    
    public Map<String, String> getBoard() { return board; }
    public void setBoard(Map<String, String> board) { this.board = board; }
    
    public long getRemainingTime() { return remainingTime; }
    public void setRemainingTime(long remainingTime) { this.remainingTime = remainingTime; }
    
    public static class PlayerDTO {
        private String id;
        private String name;
        private String color;
        private double x;
        private double y;
        private int score;
        
        public PlayerDTO() {}
        
        public PlayerDTO(Player player) {
            this.id = player.getId();
            this.name = player.getName();
            this.color = player.getColor().name();
            this.x = player.getX();
            this.y = player.getY();
            this.score = player.getScore();
        }
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        
        public double getX() { return x; }
        public void setX(double x) { this.x = x; }
        
        public double getY() { return y; }
        public void setY(double y) { this.y = y; }
        
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
    }
}
