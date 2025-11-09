package com.colorcraze.dto;

public class MoveRequest {
    private String playerId;
    private String direction;
    private boolean jump;
    
    public MoveRequest() {}
    
    public MoveRequest(String playerId, String direction, boolean jump) {
        this.playerId = playerId;
        this.direction = direction;
        this.jump = jump;
    }
    
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    
    public boolean isJump() { return jump; }
    public void setJump(boolean jump) { this.jump = jump; }
}
