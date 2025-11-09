package com.colorcraze.dto;

import java.util.List;

public class GameResultDTO {
    private String winnerId;
    private String winnerName;
    private String winnerColor;
    private List<PlayerScoreDTO> scores;
    
    public GameResultDTO() {}
    
    public String getWinnerId() { return winnerId; }
    public void setWinnerId(String winnerId) { this.winnerId = winnerId; }
    
    public String getWinnerName() { return winnerName; }
    public void setWinnerName(String winnerName) { this.winnerName = winnerName; }
    
    public String getWinnerColor() { return winnerColor; }
    public void setWinnerColor(String winnerColor) { this.winnerColor = winnerColor; }
    
    public List<PlayerScoreDTO> getScores() { return scores; }
    public void setScores(List<PlayerScoreDTO> scores) { this.scores = scores; }
    
    public static class PlayerScoreDTO {
        private String id;
        private String name;
        private String color;
        private int score;
        
        public PlayerScoreDTO() {}
        
        public PlayerScoreDTO(String id, String name, String color, int score) {
            this.id = id;
            this.name = name;
            this.color = color;
            this.score = score;
        }
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
    }
}
