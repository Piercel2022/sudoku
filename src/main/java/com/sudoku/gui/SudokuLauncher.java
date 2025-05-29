// SudokuLauncher.java - Lanceur JavaFX
package com.sudoku.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SudokuLauncher extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sudoku Game - JavaFX");
        primaryStage.setResizable(false);
        
        SudokuGUI sudokuGUI = new SudokuGUI();
        Scene scene = new Scene(sudokuGUI.getRoot(), 600, 650);
        
        // Ajout du CSS pour le style
        //scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}