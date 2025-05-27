// ============================================================================
// 2. SudokuPanel.java - Panel de la grille de jeu
// ============================================================================

// ============================================================================
// 5. SudokuPanel.java - Game display panel (should be in src/main/java/com/sudoku/gui/)
// ============================================================================
package com.sudoku.gui;

import com.sudoku.core.Sudoku;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuPanel extends JPanel {
    private Sudoku sudokuEngine;
    private JTextField[][] cells;
    private static final int CELL_SIZE = 50;
    private static final Color GIVEN_COLOR = new Color(230, 230, 230);
    private static final Color INPUT_COLOR = Color.WHITE;
    private static final Color ERROR_COLOR = new Color(255, 200, 200);
    
    public SudokuPanel(Sudoku sudokuEngine) {
        this.sudokuEngine = sudokuEngine;
        this.cells = new JTextField[9][9];
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Créer la grille 9x9 avec séparations pour les régions 3x3
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = createCell(i, j);
                
                gbc.gridx = j;
                gbc.gridy = i;
                gbc.insets = getCellInsets(i, j);
                
                add(cells[i][j], gbc);
            }
        }
        
        setBorder(BorderFactory.createRaisedBevelBorder());
    }
    
    private JTextField createCell(int row, int col) {
        JTextField cell = new JTextField();
        cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setFont(new Font("Arial", Font.BOLD, 18));
        cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Limiter à un seul chiffre
        cell.setDocument(new NumberDocument());
        
        // Action listener pour validation en temps réel
        cell.addActionListener(new CellActionListener(row, col));
        
        // Focus listener pour validation
        cell.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                validateCell(row, col);
            }
        });
        
        return cell;
    }
    
    private Insets getCellInsets(int row, int col) {
        int top = (row % 3 == 0 && row != 0) ? 3 : 1;
        int left = (col % 3 == 0 && col != 0) ? 3 : 1;
        int bottom = 1;
        int right = 1;
        
        return new Insets(top, left, bottom, right);
    }
    
    public void updateDisplay() {
        int[][] grille = sudokuEngine.getGrille();
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = cells[i][j];
                int value = grille[i][j];
                
                if (value != 0) {
                    cell.setText(String.valueOf(value));
                    cell.setBackground(GIVEN_COLOR);
                    cell.setEditable(false);
                } else {
                    cell.setText("");
                    cell.setBackground(INPUT_COLOR);
                    cell.setEditable(true);
                }
            }
        }
    }
    
    private void validateCell(int row, int col) {
        JTextField cell = cells[row][col];
        String text = cell.getText().trim();
        
        if (!text.isEmpty()) {
            try {
                int value = Integer.parseInt(text);
                if (value >= 1 && value <= 9) {
                    if (sudokuEngine.estValide(row, col, value)) {
                        cell.setBackground(INPUT_COLOR);
                        sudokuEngine.placerNombre(row, col, value);
                    } else {
                        cell.setBackground(ERROR_COLOR);
                        // Ne pas placer le nombre invalide
                    }
                } else {
                    cell.setText("");
                    cell.setBackground(INPUT_COLOR);
                }
            } catch (NumberFormatException e) {
                cell.setText("");
                cell.setBackground(INPUT_COLOR);
            }
        } else {
            cell.setBackground(INPUT_COLOR);
            sudokuEngine.retirerNombre(row, col);
        }
    }
    
    private class CellActionListener implements ActionListener {
        private int row, col;
        
        public CellActionListener(int row, int col) {
            this.row = row;
            this.col = col;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            validateCell(row, col);
        }
    }
    
    // Document pour limiter l'entrée à un seul chiffre
    private class NumberDocument extends javax.swing.text.PlainDocument {
        @Override
        public void insertString(int offset, String str, javax.swing.text.AttributeSet attr) 
                throws javax.swing.text.BadLocationException {
            
            if (str == null) return;
            
            // Permettre seulement les chiffres 1-9
            if (str.matches("[1-9]") && getLength() == 0) {
                super.insertString(offset, str, attr);
            }
        }
    }
}