// ============================================================================
// SudokuGUI.java - Main window (should be in src/main/java/com/sudoku/gui/)
// ============================================================================
package com.sudoku.gui;

import com.sudoku.core.Sudoku;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuGUI extends JFrame {
    private Sudoku sudokuEngine;
    private SudokuPanel gamePanel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    
    public SudokuGUI() {
        this.sudokuEngine = new Sudoku();
        initializeGUI();
        newGame();
    }
    
    private void initializeGUI() {
        setTitle("Sudoku Game - Java Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel principal du jeu
        gamePanel = new SudokuPanel(sudokuEngine);
        add(gamePanel, BorderLayout.CENTER);
        
        // Panel de contrôle
        createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);
        
        // Barre de menu
        createMenuBar();
        
        // Status bar
        statusLabel = new JLabel("Nouveau jeu généré - Bonne chance !");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusLabel, BorderLayout.NORTH);
        
        // Configuration de la fenêtre
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void createControlPanel() {
        controlPanel = new JPanel(new FlowLayout());
        
        JButton newGameBtn = new JButton("Nouveau Jeu");
        JButton solveBtn = new JButton("Résoudre");
        JButton hintBtn = new JButton("Indice");
        JButton checkBtn = new JButton("Vérifier");
        JButton resetBtn = new JButton("Reset");
        
        // Actions des boutons
        newGameBtn.addActionListener(e -> newGame());
        solveBtn.addActionListener(e -> solvePuzzle());
        hintBtn.addActionListener(e -> giveHint());
        checkBtn.addActionListener(e -> checkSolution());
        resetBtn.addActionListener(e -> resetGame());
        
        controlPanel.add(newGameBtn);
        controlPanel.add(solveBtn);
        controlPanel.add(hintBtn);
        controlPanel.add(checkBtn);
        controlPanel.add(resetBtn);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Jeu
        JMenu gameMenu = new JMenu("Jeu");
        JMenuItem newGameItem = new JMenuItem("Nouveau Jeu");
        JMenuItem exitItem = new JMenuItem("Quitter");
        
        newGameItem.addActionListener(e -> newGame());
        exitItem.addActionListener(e -> System.exit(0));
        
        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        
        // Menu Difficulté
        JMenu difficultyMenu = new JMenu("Difficulté");
        JMenuItem easyItem = new JMenuItem("Facile");
        JMenuItem mediumItem = new JMenuItem("Moyen");
        JMenuItem hardItem = new JMenuItem("Difficile");
        
        easyItem.addActionListener(e -> newGameWithDifficulty("facile"));
        mediumItem.addActionListener(e -> newGameWithDifficulty("moyen"));
        hardItem.addActionListener(e -> newGameWithDifficulty("difficile"));
        
        difficultyMenu.add(easyItem);
        difficultyMenu.add(mediumItem);
        difficultyMenu.add(hardItem);
        
        // Menu Aide
        JMenu helpMenu = new JMenu("Aide");
        JMenuItem aboutItem = new JMenuItem("À propos");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        
        menuBar.add(gameMenu);
        menuBar.add(difficultyMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void newGame() {
        sudokuEngine.genererGrille();
        gamePanel.updateDisplay();
        statusLabel.setText("Nouveau jeu généré - Bonne chance !");
    }
    
    private void newGameWithDifficulty(String difficulty) {
        // Adapter selon votre implémentation de difficulté
        newGame();
        statusLabel.setText("Nouveau jeu (" + difficulty + ") généré !");
    }
    
    private void solvePuzzle() {
        if (sudokuEngine.resoudre()) {
            gamePanel.updateDisplay();
            statusLabel.setText("Puzzle résolu automatiquement !");
            JOptionPane.showMessageDialog(this, "Puzzle résolu !", "Résolution", JOptionPane.INFORMATION_MESSAGE);
        } else {
            statusLabel.setText("Impossible de résoudre ce puzzle.");
            JOptionPane.showMessageDialog(this, "Impossible de résoudre ce puzzle.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void giveHint() {
        // Implémentation d'indice - trouve une cellule vide et donne la solution
        int[][] grille = sudokuEngine.getGrille();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grille[i][j] == 0) {
                    // Trouve la valeur correcte pour cette cellule
                    for (int num = 1; num <= 9; num++) {
                        if (sudokuEngine.estValide(i, j, num)) {
                            sudokuEngine.placerNombre(i, j, num);
                            gamePanel.updateDisplay();
                            statusLabel.setText("Indice donné en (" + (i+1) + "," + (j+1) + ") = " + num);
                            return;
                        }
                    }
                }
            }
        }
        statusLabel.setText("Aucun indice disponible.");
    }
    
    private void checkSolution() {
        if (sudokuEngine.estComplete()) {
            if (sudokuEngine.estValide()) {
                statusLabel.setText("Félicitations ! Puzzle résolu correctement !");
                JOptionPane.showMessageDialog(this, "Félicitations !\nVous avez résolu le puzzle !", 
                    "Victoire !", JOptionPane.INFORMATION_MESSAGE);
            } else {
                statusLabel.setText("Il y a des erreurs dans votre solution.");
                JOptionPane.showMessageDialog(this, "Il y a des erreurs dans votre solution.", 
                    "Erreurs détectées", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            statusLabel.setText("Puzzle non terminé.");
        }
    }
    
    private void resetGame() {
        // Reset à l'état initial du puzzle
        sudokuEngine.genererGrille();
        gamePanel.updateDisplay();
        statusLabel.setText("Jeu remis à zéro.");
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this, 
            "Sudoku Game v1.0\n" +
            "Développé en Java Swing\n" +
            "Architecture modulaire\n\n" +
            "Règles du Sudoku :\n" +
            "- Remplir la grille 9x9\n" +
            "- Chaque ligne, colonne et région 3x3\n" +
            "  doit contenir les chiffres 1-9\n" +
            "- Pas de répétition !",
            "À propos", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        // Simple version without Look and Feel setting
        SwingUtilities.invokeLater(() -> {
            new SudokuGUI().setVisible(true);
        });
    }
}