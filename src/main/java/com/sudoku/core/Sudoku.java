// Sudoku.java - Classe principale de logique métier
package com.sudoku.core;

public class Sudoku {
    private int[][] grid;
    private int[][] solution;
    private static final int SIZE = 9;
    private static final int EMPTY = 0;
    
    public Sudoku() {
        this.grid = new int[SIZE][SIZE];
        this.solution = new int[SIZE][SIZE];
        generatePuzzle();
    }
    
    public Sudoku(int[][] initialGrid) {
        this.grid = new int[SIZE][SIZE];
        this.solution = new int[SIZE][SIZE];
        
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(initialGrid[i], 0, this.grid[i], 0, SIZE);
            System.arraycopy(initialGrid[i], 0, this.solution[i], 0, SIZE);
        }
        
        solveSudoku(this.solution);
    }
    
    private void generatePuzzle() {
        // Génère une grille complète
        generateCompleteSudoku(grid);
        System.arraycopy(grid, 0, solution, 0, grid.length);
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(grid[i], 0, solution[i], 0, SIZE);
        }
        
        // Supprime des cellules pour créer le puzzle
        removeCells();
    }
    
    private boolean generateCompleteSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValidMove(board, row, col, num)) {
                            board[row][col] = num;
                            if (generateCompleteSudoku(board)) {
                                return true;
                            }
                            board[row][col] = EMPTY;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    private void removeCells() {
        int cellsToRemove = 40; // Difficulté moyenne
        java.util.Random random = new java.util.Random();
        
        while (cellsToRemove > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            
            if (grid[row][col] != EMPTY) {
                grid[row][col] = EMPTY;
                cellsToRemove--;
            }
        }
    }
    
    public boolean isValidMove(int row, int col, int num) {
        return isValidMove(grid, row, col, num);
    }
    
    private boolean isValidMove(int[][] board, int row, int col, int num) {
        // Vérifier la ligne
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num) {
                return false;
            }
        }
        
        // Vérifier la colonne
        for (int x = 0; x < SIZE; x++) {
            if (board[x][col] == num) {
                return false;
            }
        }
        
        // Vérifier le carré 3x3
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValidMove(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][col] = EMPTY;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    public void setCell(int row, int col, int value) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            grid[row][col] = value;
        }
    }
    
    public int getCell(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            return grid[row][col];
        }
        return -1;
    }
    
    public boolean isSolved() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == EMPTY || grid[row][col] != solution[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void reset() {
        generatePuzzle();
    }
    
    public int[][] getGrid() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }
    
    public static int getSize() {
        return SIZE;
    }
}