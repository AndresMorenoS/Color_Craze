package com.colorcraze.dto;

import com.colorcraze.model.PlayerColor;

public class JoinGameRequest {
    private String playerName;
    private PlayerColor color;
    
    public JoinGameRequest() {}
    
    public JoinGameRequest(String playerName, PlayerColor color) {
        this.playerName = playerName;
        this.color = color;
    }
    
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    
    public PlayerColor getColor() { return color; }
    public void setColor(PlayerColor color) { this.color = color; }
}
