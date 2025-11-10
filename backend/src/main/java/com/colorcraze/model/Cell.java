package com.colorcraze.model;

public class Cell {
    private int x;
    private int y;
    private PlayerColor color;
    private boolean paintable;
    
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = null;
        this.paintable = true; // default to paintable
    }
    
    public Cell(int x, int y, boolean paintable) {
        this.x = x;
        this.y = y;
        this.color = null;
        this.paintable = paintable;
    }
    
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    
    public PlayerColor getColor() { return color; }
    public void setColor(PlayerColor color) { this.color = color; }
    
    public boolean isPaintable() { return paintable; }
    public void setPaintable(boolean paintable) { this.paintable = paintable; }
}
