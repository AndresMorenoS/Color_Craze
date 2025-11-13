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
                boolean isPaintable = isPaintableCell(x, y);
                cells[y][x] = new Cell(x, y, isPaintable);
            }
        }
    }
    
    /**
     * Determines if a cell should be paintable.
     * Paintable cells include:
     * - Floor (bottom 1 row)
     * - Left and right walls (leftmost and rightmost 1 column)
     * - Ceiling (top 1 row)
     * - Suspended platforms in the middle area (1 block high)
     */
    private boolean isPaintableCell(int x, int y) {
        // Floor: bottom 1 row
        if (y >= height - 1) {
            return true;
        }
        
        // Ceiling: top 1 row
        if (y == 0) {
            return true;
        }
        
        // Left wall: leftmost 1 column
        if (x == 0) {
            return true;
        }
        
        // Right wall: rightmost 1 column
        if (x >= width - 1) {
            return true;
        }
        
        // Suspended platforms - add some floating platforms (1 block high)
        // Platform 1: middle-left area
        if (y == 10 && x >= 8 && x <= 15) {
            return true;
        }
        
        // Platform 2: middle-right area
        if (y == 10 && x >= 24 && x <= 31) {
            return true;
        }
        
        // Platform 3: upper-middle area
        if (y == 7 && x >= 15 && x <= 24) {
            return true;
        }
        
        // Platform 4: lower-middle platforms (left)
        if (y == 20 && x >= 10 && x <= 17) {
            return true;
        }
        
        // Platform 5: lower-middle platforms (right)
        if (y == 20 && x >= 22 && x <= 29) {
            return true;
        }
        
        // Everything else is not paintable (air)
        return false;
    }
    
    public void paintCell(int x, int y, PlayerColor color) {
        if (isValidPosition(x, y) && cells[y][x].isPaintable()) {
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
    
    public boolean isCellPaintable(int x, int y) {
        if (isValidPosition(x, y)) {
            return cells[y][x].isPaintable();
        }
        return false;
    }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Cell[][] getCells() { return cells; }
}
