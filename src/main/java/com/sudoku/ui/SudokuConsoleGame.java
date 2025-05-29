// SudokuConsoleGame.java - Version console
package com.sudoku.ui;

import com.sudoku.core.Sudoku;
import java.util.Scanner;

public class SudokuConsoleGame {
    private Sudoku sudoku;
    private Scanner scanner;
    
    public SudokuConsoleGame() {
        sudoku = new Sudoku();
        scanner = new Scanner(System.in);
    }
    
    public void play() {
        System.out.println("=== JEU SUDOKU CONSOLE ===");
        
        while (!sudoku.isSolved()) {
            printGrid();
            System.out.println("\nCommandes: [ligne] [colonne] [valeur] ou 'quit' pour quitter");
            System.out.print("Votre choix: ");
            
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Au revoir !");
                break;
            }
            
            try {
                String[] parts = input.split(" ");
                if (parts.length == 3) {
                    int row = Integer.parseInt(parts[0]) - 1;
                    int col = Integer.parseInt(parts[1]) - 1;
                    int value = Integer.parseInt(parts[2]);
                    
                    if (sudoku.isValidMove(row, col, value)) {
                        sudoku.setCell(row, col, value);
                        System.out.println("Bon mouvement !");
                    } else {
                        System.out.println("Mouvement invalide !");
                    }
                } else {
                    System.out.println("Format invalide. Utilisez: ligne colonne valeur");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer des nombres valides.");
            }
        }
        
        if (sudoku.isSolved()) {
            System.out.println("\n🎉 Félicitations ! Vous avez résolu le Sudoku ! 🎉");
            printGrid();
        }
        
        scanner.close();
    }
    
    private void printGrid() {
        System.out.println("\n   1 2 3   4 5 6   7 8 9");
        System.out.println("  ┌─────┬─────┬─────┐");
        
        for (int row = 0; row < 9; row++) {
            if (row == 3 || row == 6) {
                System.out.println("  ├─────┼─────┼─────┤");
            }
            
            System.out.print((row + 1) + " │");
            
            for (int col = 0; col < 9; col++) {
                if (col == 3 || col == 6) {
                    System.out.print("│");
                }
                
                int value = sudoku.getCell(row, col);
                if (value == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print(value);
                }
                
                if (col < 8) System.out.print(" ");
            }
            System.out.println("│");
        }
        
        System.out.println("  └─────┴─────┴─────┘");
    }
    
    public static void main(String[] args) {
        SudokuConsoleGame game = new SudokuConsoleGame();
        game.play();
    }
}