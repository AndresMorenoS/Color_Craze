package com.colorcraze.dto;

public class CreateGameResponse {
    private String gameCode;
    private String playerId;
    
    public CreateGameResponse() {}
    
    public CreateGameResponse(String gameCode, String playerId) {
        this.gameCode = gameCode;
        this.playerId = playerId;
    }
    
    public String getGameCode() { return gameCode; }
    public void setGameCode(String gameCode) { this.gameCode = gameCode; }
    
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
}
