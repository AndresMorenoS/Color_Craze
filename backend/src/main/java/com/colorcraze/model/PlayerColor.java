package com.colorcraze.model;

public enum PlayerColor {
    RED("#FF0000"),
    BLUE("#0000FF"),
    GREEN("#00FF00"),
    YELLOW("#FFFF00");
    
    private final String hexCode;
    
    PlayerColor(String hexCode) {
        this.hexCode = hexCode;
    }
    
    public String getHexCode() {
        return hexCode;
    }
}
