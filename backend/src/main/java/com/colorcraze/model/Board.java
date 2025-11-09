package com.colorcraze.model;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private final int width;
    private final int height;
    private final Cell[][] cells;
    
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[height][width];
        initializeCells();
    }
    
    private void initializeCells() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = new Cell(x, y);
            }
        }
    }
    
    public void paintCell(int x, int y, PlayerColor color) {
        if (isValidPosition(x, y)) {
            cells[y][x].setColor(color);
        }
    }
    
    public PlayerColor getCellColor(int x, int y) {
        if (isValidPosition(x, y)) {
            return cells[y][x].getColor();
        }
        return null;
    }
    
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    public Map<PlayerColor, Integer> calculateScores() {
        Map<PlayerColor, Integer> scores = new HashMap<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                PlayerColor color = cells[y][x].getColor();
                if (color != null) {
                    scores.put(color, scores.getOrDefault(color, 0) + 1);
                }
            }
        }
        return scores;
    }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Cell[][] getCells() { return cells; }
}
