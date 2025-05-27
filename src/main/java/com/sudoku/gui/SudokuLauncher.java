// ============================================================================
// 3. SudokuLauncher.java - Point d'entrée GUI
// ============================================================================
package main.java.com.sudoku.gui;

import javax.swing.*;

public class SudokuLauncher {
    public static void main(String[] args) {
        // Définir le Look and Feel du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            System.err.println("Impossible de définir le Look and Feel: " + e.getMessage());
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

// ============================================================================
// 4. Adapter votre classe Sudoku (ajouts nécessaires)
// ============================================================================
/*
Ajoutez ces méthodes à votre classe Sudoku existante :

public void placerNombre(int ligne, int colonne, int nombre) {
    if (ligne >= 0 && ligne < 9 && colonne >= 0 && colonne < 9) {
        grille[ligne][colonne] = nombre;
    }
}

public void retirerNombre(int ligne, int colonne) {
    if (ligne >= 0 && ligne < 9 && colonne >= 0 && colonne < 9) {
        grille[ligne][colonne] = 0;
    }
}

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

public boolean estValide() {
    // Vérifier toute la grille
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

