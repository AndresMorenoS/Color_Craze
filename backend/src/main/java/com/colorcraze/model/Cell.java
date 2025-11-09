package com.colorcraze.model;

public class Cell {
    private int x;
    private int y;
    private PlayerColor color;
    
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = null;
    }
    
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    
    public PlayerColor getColor() { return color; }
    public void setColor(PlayerColor color) { this.color = color; }
}
