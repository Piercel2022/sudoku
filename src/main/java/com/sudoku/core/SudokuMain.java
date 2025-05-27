// ============================================================================
// SudokuMain.java - Main class (src/main/java/com/sudoku/core/SudokuMain.java)
// ============================================================================
package com.sudoku.core;

import com.sudoku.ui.SudokuConsoleGame;

public class SudokuMain {
    public static void main(String[] args) {
        // Exemple d'utilisation directe de la classe Sudoku
        System.out.println("=== DÉMONSTRATION DU SUDOKU ===\n");
        
        // Créer une instance
        Sudoku demo = new Sudoku();
        
        // Générer et afficher un puzzle
        System.out.println("1. Génération d'un puzzle moyen:");
        demo.createPuzzle(40);
        demo.printGrid();
        
        // Résoudre le puzzle
        System.out.println("\n2. Résolution du puzzle:");
        if (demo.solveSudoku()) {
            demo.printGrid();
            System.out.println("✅ Puzzle résolu avec succès!");
        }
        
        // Test avec un puzzle prédéfini
        System.out.println("\n3. Test avec un puzzle prédéfini:");
        int[][] testPuzzle = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        
        demo.loadGrid(testPuzzle);
        System.out.println("Puzzle de test:");
        demo.printGrid();
        
        if (demo.solveSudoku()) {
            System.out.println("\nSolution:");
            demo.printGrid();
        }
        
        // Lancer le jeu interactif
        System.out.println("\n=== LANCEMENT DU JEU INTERACTIF ===");
        SudokuConsoleGame game = new SudokuConsoleGame();
        game.playGame();
    }
}