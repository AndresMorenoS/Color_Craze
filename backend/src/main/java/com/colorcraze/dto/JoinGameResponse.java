package com.colorcraze.dto;

public class JoinGameResponse {
    private String playerId;
    private boolean success;
    private String message;
    private String color;
    
    public JoinGameResponse() {}
    
    public JoinGameResponse(String playerId, boolean success, String message, String color) {
        this.playerId = playerId;
        this.success = success;
        this.message = message;
        this.color = color;
    }
    
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
