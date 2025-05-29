// SudokuPanel.java - Panel de la grille de jeu
package com.sudoku.gui;

import com.sudoku.core.Sudoku;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SudokuPanel {
    private Sudoku sudoku;
    private SudokuGUI parentGUI;
    private GridPane gridPane;
    private TextField[][] cells;
    private int[][] initialGrid;
    
    public SudokuPanel(Sudoku sudoku, SudokuGUI parentGUI) {
        this.sudoku = sudoku;
        this.parentGUI = parentGUI;
        this.cells = new TextField[9][9];
        
        // Sauvegarder la grille initiale
        saveInitialGrid();
        
        initializeGrid();
        updateGrid();
    }
    
    private void saveInitialGrid() {
        initialGrid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                initialGrid[i][j] = sudoku.getCell(i, j);
            }
        }
    }
    
    private void initializeGrid() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        gridPane.setStyle("-fx-background-color: #333333; -fx-padding: 5;");
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = createCell(row, col);
                cells[row][col] = cell;
                gridPane.add(cell, col, row);
            }
        }
    }
    
    private TextField createCell(int row, int col) {
        TextField cell = new TextField();
        cell.setPrefSize(50, 50);
        cell.setAlignment(Pos.CENTER);
        cell.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Create the complete base style as a final variable
        final String baseStyle = getBaseCellStyle(row, col);
        
        cell.setStyle(baseStyle + "-fx-background-color: white;");
        
        // Gestionnaire d'événements pour la saisie
        final int finalRow = row;
        final int finalCol = col;
        
        cell.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 1) {
                cell.setText(oldValue);
                return;
            }
            
            if (!newValue.matches("[1-9]?")) {
                cell.setText(oldValue);
                return;
            }
            
            if (newValue.isEmpty()) {
                sudoku.setCell(finalRow, finalCol, 0);
                cell.setStyle(baseStyle + "-fx-background-color: white; -fx-text-fill: blue;");
            } else {
                int value = Integer.parseInt(newValue);
                if (sudoku.isValidMove(finalRow, finalCol, value)) {
                    sudoku.setCell(finalRow, finalCol, value);
                    cell.setStyle(baseStyle + "-fx-background-color: white; -fx-text-fill: blue;");
                    parentGUI.updateStatus("Bon mouvement !");
                } else {
                    cell.setStyle(baseStyle + "-fx-background-color: #ffcccc; -fx-text-fill: red;");
                    parentGUI.updateStatus("Mouvement invalide !");
                }
            }
            
            // Vérifier si le puzzle est résolu
            if (sudoku.isSolved()) {
                parentGUI.updateStatus("");
            }
        });
        
        return cell;
    }
    
    public void updateGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = sudoku.getCell(row, col);
                TextField cell = cells[row][col];
                
                if (value == 0) {
                    cell.setText("");
                    cell.setEditable(true);
                    cell.setStyle(getBaseCellStyle(row, col) + "-fx-background-color: white; -fx-text-fill: blue;");
                } else {
                    cell.setText(String.valueOf(value));
                    
                    // Cellules préremplies (non modifiables)
                    if (initialGrid[row][col] != 0) {
                        cell.setEditable(false);
                        cell.setStyle(getBaseCellStyle(row, col) + "-fx-background-color: #f0f0f0; -fx-text-fill: black;");
                    } else {
                        cell.setEditable(true);
                        cell.setStyle(getBaseCellStyle(row, col) + "-fx-background-color: white; -fx-text-fill: blue;");
                    }
                }
            }
        }
    }
    
    private String getBaseCellStyle(int row, int col) {
        String baseStyle = "-fx-border-width: 1; -fx-border-color: black;";
        
        if (row % 3 == 0) baseStyle += "-fx-border-width: 3 1 1 1;";
        if (col % 3 == 0) baseStyle += "-fx-border-width: 1 1 1 3;";
        if (row % 3 == 2) baseStyle += "-fx-border-width: 1 1 3 1;";
        if (col % 3 == 2) baseStyle += "-fx-border-width: 1 3 1 1;";
        
        return baseStyle;
    }
    
    public void resetToInitial() {
        // Restaurer la grille initiale
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                sudoku.setCell(row, col, initialGrid[row][col]);
            }
        }
        updateGrid();
    }
    
    public GridPane getGridPane() {
        return gridPane;
    }
}