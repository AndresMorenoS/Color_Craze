package com.colorcraze.model;

public class Player {
    private String id;
    private String name;
    private PlayerColor color;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private boolean isJumping;
    private int score;
    
    public Player(String id, String name, PlayerColor color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.x = 0;
        this.y = 0;
        this.velocityX = 0;
        this.velocityY = 0;
        this.isJumping = false;
        this.score = 0;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public PlayerColor getColor() { return color; }
    public void setColor(PlayerColor color) { this.color = color; }
    
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    
    public double getVelocityX() { return velocityX; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    
    public double getVelocityY() { return velocityY; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
    
    public boolean isJumping() { return isJumping; }
    public void setJumping(boolean jumping) { isJumping = jumping; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
