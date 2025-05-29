// SudokuGUI.java - Interface graphique principale
package com.sudoku.gui;

import com.sudoku.core.Sudoku;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SudokuGUI {
    private Sudoku sudoku;
    private SudokuPanel sudokuPanel;
    private BorderPane root;
    private Label statusLabel;
    
    public SudokuGUI() {
        this.statusLabel = new Label("Ready");
        sudoku = new Sudoku();
        initializeGUI();
    }
    
    private void initializeGUI() {
        root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Panel supérieur avec titre et boutons
        VBox topPanel = createTopPanel();
        root.setTop(topPanel);
        
        // Panel central avec la grille Sudoku
        sudokuPanel = new SudokuPanel(sudoku, this);
        root.setCenter(sudokuPanel.getGridPane());
        
        // Panel inférieur avec status
        HBox bottomPanel = createBottomPanel();
        root.setBottom(bottomPanel);
    }
    
    private VBox createTopPanel() {
        VBox topPanel = new VBox(10);
        topPanel.setAlignment(Pos.CENTER);
        
        // Titre
        Label titleLabel = new Label("SUDOKU");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKBLUE);
        
        // Boutons
        HBox buttonPanel = new HBox(10);
        buttonPanel.setAlignment(Pos.CENTER);
        
        Button newGameBtn = new Button("Nouvelle Partie");
        Button solveBtn = new Button("Résoudre");
        Button resetBtn = new Button("Réinitialiser");
        Button hintBtn = new Button("Indice");
        
        // Style des boutons
        String buttonStyle = "-fx-font-size: 12px; -fx-padding: 8 16 8 16;";
        newGameBtn.setStyle(buttonStyle + "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        solveBtn.setStyle(buttonStyle + "-fx-background-color: #2196F3; -fx-text-fill: white;");
        resetBtn.setStyle(buttonStyle + "-fx-background-color: #FF9800; -fx-text-fill: white;");
        hintBtn.setStyle(buttonStyle + "-fx-background-color: #9C27B0; -fx-text-fill: white;");
        
        // Actions des boutons
        newGameBtn.setOnAction(e -> newGame());
        solveBtn.setOnAction(e -> solvePuzzle());
        resetBtn.setOnAction(e -> resetGame());
        hintBtn.setOnAction(e -> showHint());
        
        buttonPanel.getChildren().addAll(newGameBtn, solveBtn, resetBtn, hintBtn);
        topPanel.getChildren().addAll(titleLabel, buttonPanel);
        
        return topPanel;
    }
    
    private HBox createBottomPanel() {
        HBox bottomPanel = new HBox();
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(10, 0, 0, 0));
        
        statusLabel = new Label("Bonne chance !");
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        statusLabel.setTextFill(Color.DARKGREEN);
        
        bottomPanel.getChildren().add(statusLabel);
        return bottomPanel;
    }
    
    private void newGame() {
        sudoku.reset();
        sudokuPanel.updateGrid();
        updateStatus("Nouvelle partie commencée !");
    }
    
    private void solvePuzzle() {
        int[][] grid = sudoku.getGrid();
        if (sudoku.solveSudoku(grid)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    sudoku.setCell(i, j, grid[i][j]);
                }
            }
            sudokuPanel.updateGrid();
            updateStatus("Puzzle résolu !");
        } else {
            updateStatus("Impossible de résoudre ce puzzle.");
        }
    }
    
    private void resetGame() {
        // Réinitialiser avec la même grille initiale
        sudokuPanel.resetToInitial();
        updateStatus("Grille réinitialisée !");
    }
    
    private void showHint() {
        // Trouver une cellule vide et donner un indice
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (sudoku.getCell(row, col) == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (sudoku.isValidMove(row, col, num)) {
                            updateStatus("Indice: Essayez " + num + " à la ligne " + (row + 1) + ", colonne " + (col + 1));
                            return;
                        }
                    }
                }
            }
        }
        updateStatus("Aucun indice disponible.");
    }
    
    public void updateStatus(String message) {
        statusLabel.setText(message);
        
        if (sudoku.isSolved()) {
            statusLabel.setText("🎉 Félicitations ! Vous avez résolu le Sudoku ! 🎉");
            statusLabel.setTextFill(Color.GREEN);
        }
    }
    
    public Parent getRoot() {
        return root;
    }
}
