package com.colorcraze.dto;

public class CreateGameResponse {
    private String gameCode;
    private String playerId;
    private String color;
    
    public CreateGameResponse() {}
    
    public CreateGameResponse(String gameCode, String playerId, String color) {
        this.gameCode = gameCode;
        this.playerId = playerId;
        this.color = color;
    }
    
    public String getGameCode() { return gameCode; }
    public void setGameCode(String gameCode) { this.gameCode = gameCode; }
    
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
