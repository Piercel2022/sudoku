// ============================================================================
// SudokuLauncher.java - GUI entry point (src/main/java/com/sudoku/gui/SudokuLauncher.java)
// ============================================================================
package com.sudoku.gui;


import javax.swing.*;

public class SudokuLauncher {
    public static void main(String[] args) {
        // Définir le Look and Feel du système
        try {
            // Utiliser getSystemLookAndFeelClassName() qui retourne une String
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Impossible de définir le Look and Feel système: " + e.getMessage());
            // Fallback vers le Look and Feel cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Utilisation du Look and Feel par défaut");
            }
        }
        
        // Lancer l'interface graphique dans l'EDT
        SwingUtilities.invokeLater(() -> {
            try {
                SudokuGUI gui = new SudokuGUI();
                gui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Erreur lors du lancement de l'application: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}