// ============================================================================
// Sudoku.java - Core Sudoku engine (src/main/java/com/sudoku/core/Sudoku.java)
// ============================================================================
package com.sudoku.core;

import java.util.Random;
import java.util.Arrays;

public class Sudoku {
    private int[][] grille;
    private int[][] originalGrid;
    private Random random;
    
    public Sudoku() {
        this.grille = new int[9][9];
        this.originalGrid = new int[9][9];
        this.random = new Random();
        clearGrid();
    }
    
    /**
     * Initialise la grille avec des zéros
     */
    public void clearGrid() {
        for (int i = 0; i < 9; i++) {
            Arrays.fill(grille[i], 0);
            Arrays.fill(originalGrid[i], 0);
        }
    }
    
    /**
     * Retourne une copie de la grille
     */
    public int[][] getGrille() {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(grille[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
    
    /**
     * Copie la grille actuelle
     */
    public int[][] copyGrid() {
        return getGrille();
    }
    
    /**
     * Charge une grille depuis un tableau 2D
     */
    public void loadGrid(int[][] newGrid) {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(newGrid[i], 0, grille[i], 0, 9);
        }
    }
    
    /**
     * Place un nombre dans la grille
     */
    public void placerNombre(int ligne, int colonne, int nombre) {
        if (ligne >= 0 && ligne < 9 && colonne >= 0 && colonne < 9) {
            grille[ligne][colonne] = nombre;
        }
    }
    
    /**
     * Retire un nombre de la grille
     */
    public void retirerNombre(int ligne, int colonne) {
        if (ligne >= 0 && ligne < 9 && colonne >= 0 && colonne < 9) {
            grille[ligne][colonne] = 0;
        }
    }
    
    /**
     * Définit un nombre dans la grille (avec validation)
     */
    public boolean setNumber(int row, int col, int number) {
        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            return false;
        }
        
        if (number == 0) {
            grille[row][col] = 0;
            return true;
        }
        
        if (number < 1 || number > 9) {
            return false;
        }
        
        if (estValide(row, col, number)) {
            grille[row][col] = number;
            return true;
        }
        
        return false;
    }
    
    /**
     * Vérifie si un nombre peut être placé à une position donnée
     */
    public boolean estValide(int ligne, int colonne, int nombre) {
        if (nombre < 1 || nombre > 9) {
            return false;
        }
        
        // Vérifier la ligne
        for (int col = 0; col < 9; col++) {
            if (col != colonne && grille[ligne][col] == nombre) {
                return false;
            }
        }
        
        // Vérifier la colonne
        for (int row = 0; row < 9; row++) {
            if (row != ligne && grille[row][colonne] == nombre) {
                return false;
            }
        }
        
        // Vérifier la région 3x3
        int startRow = (ligne / 3) * 3;
        int startCol = (colonne / 3) * 3;
        
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                if ((row != ligne || col != colonne) && grille[row][col] == nombre) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Vérifie si la grille est complète
     */
    public boolean estComplete() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grille[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Vérifie si la grille actuelle est valide
     */
    public boolean estValide() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grille[i][j] != 0) {
                    int temp = grille[i][j];
                    grille[i][j] = 0; // Temporairement vide pour tester
                    if (!estValide(i, j, temp)) {
                        grille[i][j] = temp; // Restaurer
                        return false;
                    }
                    grille[i][j] = temp; // Restaurer
                }
            }
        }
        return true;
    }
    
    /**
     * Vérifie si le Sudoku est résolu
     */
    public boolean isSolved() {
        return estComplete() && estValide();
    }
    
    /**
     * Génère une grille complète valide
     */
    public void genererGrille() {
        clearGrid();
        generateCompleteGrid();
        saveOriginalGrid();
    }
    
    /**
     * Crée un puzzle en retirant des nombres
     */
    public void createPuzzle(int cellsToRemove) {
        genererGrille();
        removeCells(cellsToRemove);
        saveOriginalGrid();
    }
    
    /**
     * Sauvegarde la grille originale
     */
    private void saveOriginalGrid() {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(grille[i], 0, originalGrid[i], 0, 9);
        }
    }
    
    /**
     * Génère une grille complète valide
     */
    private void generateCompleteGrid() {
        // Remplir la diagonale (3 régions 3x3 indépendantes)
        fillDiagonal();
        
        // Remplir le reste de la grille
        solveSudoku();
    }
    
    /**
     * Remplit les 3 régions diagonales
     */
    private void fillDiagonal() {
        for (int i = 0; i < 9; i += 3) {
            fillBox(i, i);
        }
    }
    
    /**
     * Remplit une région 3x3
     */
    private void fillBox(int startRow, int startCol) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        shuffleArray(numbers);
        
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grille[startRow + i][startCol + j] = numbers[index++];
            }
        }
    }
    
    /**
     * Mélange un tableau
     */
    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
    
    /**
     * Résout le Sudoku avec backtracking
     */
    public boolean solveSudoku() {
        return resoudre();
    }
    
    /**
     * Résout le Sudoku (alias pour compatibilité)
     */
    public boolean resoudre() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grille[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (estValide(row, col, num)) {
                            grille[row][col] = num;
                            
                            if (resoudre()) {
                                return true;
                            }
                            
                            grille[row][col] = 0; // Backtrack
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Retire des cellules pour créer un puzzle
     */
    private void removeCells(int count) {
        int removed = 0;
        while (removed < count) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            
            if (grille[row][col] != 0) {
                int temp = grille[row][col];
                grille[row][col] = 0;
                
                // Vérifier que le puzzle a toujours une solution unique
                if (hasUniqueSolution()) {
                    removed++;
                } else {
                    grille[row][col] = temp; // Restaurer si pas de solution unique
                }
            }
        }
    }
    
    /**
     * Vérifie si le puzzle a une solution unique
     */
    private boolean hasUniqueSolution() {
        int[][] backup = copyGrid();
        int solutions = countSolutions(0);
        loadGrid(backup);
        return solutions == 1;
    }
    
    /**
     * Compte le nombre de solutions possibles
     */
    private int countSolutions(int solutions) {
        if (solutions > 1) return solutions; // Optimisation
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grille[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (estValide(row, col, num)) {
                            grille[row][col] = num;
                            solutions = countSolutions(solutions);
                            grille[row][col] = 0;
                            
                            if (solutions > 1) return solutions;
                        }
                    }
                    return solutions;
                }
            }
        }
        return solutions + 1;
    }
    
    /**
     * Affiche la grille dans la console
     */
    public void printGrid() {
        System.out.println("╔═══════╤═══════╤═══════╗");
        
        for (int i = 0; i < 9; i++) {
            if (i == 3 || i == 6) {
                System.out.println("╟───────┼───────┼───────╢");
            }
            
            System.out.print("║ ");
            for (int j = 0; j < 9; j++) {
                if (j == 3 || j == 6) {
                    System.out.print("│ ");
                }
                
                if (grille[i][j] == 0) {
                    System.out.print("· ");
                } else {
                    System.out.print(grille[i][j] + " ");
                }
            }
            System.out.println("║");
        }
        
        System.out.println("╚═══════╧═══════╧═══════╝");
    }
}