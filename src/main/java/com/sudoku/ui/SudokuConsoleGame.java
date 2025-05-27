// ==================== ÉTAPE 6: Interface utilisateur console ====================

public class SudokuConsoleGame {
    private Sudoku sudoku;
    private Scanner scanner;
    
    public SudokuConsoleGame() {
        this.sudoku = new Sudoku();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Lance le jeu principal
     */
    public void playGame() {
        System.out.println("=== JEU SUDOKU ===");
        System.out.println();
        
        while (true) {
            showMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    playNewGame();
                    break;
                case 2:
                    solveCurrentPuzzle();
                    break;
                case 3:
                    enterCustomPuzzle();
                    break;
                case 4:
                    System.out.println("Merci d'avoir joué!");
                    return;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Nouveau jeu");
        System.out.println("2. Résoudre le puzzle actuel");
        System.out.println("3. Entrer un puzzle personnalisé");
        System.out.println("4. Quitter");
        System.out.print("Votre choix: ");
    }
    
    private int getChoice() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Clear invalid input
            return -1;
        }
    }

    private void playNewGame() {
        System.out.println("\n=== NOUVEAU JEU ===");
        System.out.println("Choisissez la difficulté:");
        System.out.println("1. Facile (30 cases vides)");
        System.out.println("2. Moyen (40 cases vides)");
        System.out.println("3. Difficile (50 cases vides)");
        System.out.print("Votre choix: ");
        
        int difficulty = getChoice();
        int cellsToRemove;
        
        switch (difficulty) {
            case 1: cellsToRemove = 30; break;
            case 2: cellsToRemove = 40; break;
            case 3: cellsToRemove = 50; break;
            default: cellsToRemove = 40; break;
        }
        
        System.out.println("Génération du puzzle...");
        sudoku.createPuzzle(cellsToRemove);
        
        playInteractiveGame();
    }

    private void playInteractiveGame() {
        while (true) {
            System.out.println("\n=== GRILLE ACTUELLE ===");
            sudoku.printGrid();
            
            if (sudoku.isSolved()) {
                System.out.println("\n🎉 FÉLICITATIONS! Vous avez résolu le Sudoku! 🎉");
                break;
            }
            
            System.out.println("\nCommandes:");
            System.out.println("- Placer un nombre: ligne colonne nombre (ex: 1 2 5)");
            System.out.println("- Effacer une case: ligne colonne 0");
            System.out.println("- Retour au menu: menu");
            System.out.print("Votre action: ");
            
            String input = scanner.nextLine().trim();
            
            if (input.equals("menu")) {
                break;
            }
            
            String[] parts = input.split(" ");
            if (parts.length == 3) {
                try {
                    int row = Integer.parseInt(parts[0]) - 1; // Conversion 1-9 vers 0-8
                    int col = Integer.parseInt(parts[1]) - 1;
                    int number = Integer.parseInt(parts[2]);
                    
                    if (sudoku.setNumber(row, col, number)) {
                        System.out.println("✅ Nombre placé avec succès!");
                    } else {
                        System.out.println("❌ Placement invalide!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Format invalide! Utilisez: ligne colonne nombre");
                }
            } else {
                System.out.println("❌ Format invalide! Utilisez: ligne colonne nombre");
            }
        }
    }
    
    private void solveCurrentPuzzle() {
        System.out.println("\n=== RÉSOLUTION AUTOMATIQUE ===");
        System.out.println("Grille avant résolution:");
        sudoku.printGrid();
        
        // Sauvegarder l'état actuel
        int[][] originalGrid = sudoku.copyGrid();
        
        if (sudoku.solveSudoku()) {
            System.out.println("\n✅ Solution trouvée:");
            sudoku.printGrid();
        } else {
            System.out.println("\n❌ Aucune solution trouvée pour ce puzzle!");
            sudoku.loadGrid(originalGrid); // Restaurer l'état original
        }
    }
    
    private void enterCustomPuzzle() {
        System.out.println("\n=== PUZZLE PERSONNALISÉ ===");
        System.out.println("Entrez votre puzzle ligne par ligne.");
        System.out.println("Utilisez 0 pour les cases vides, 1-9 pour les nombres.");
        System.out.println("Exemple: 1 0 3 0 5 0 7 0 9");
        
        sudoku.clearGrid();
        
        for (int row = 0; row < 9; row++) {
            System.out.printf("Ligne %d: ", row + 1);
            String input = scanner.nextLine().trim();
            String[] numbers = input.split(" ");
            
            if (numbers.length == 9) {
                try {
                    for (int col = 0; col < 9; col++) {
                        int number = Integer.parseInt(numbers[col]);
                        sudoku.setNumber(row, col, number);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Erreur dans le format. Recommencez cette ligne.");
                    row--; // Recommencer cette ligne
                }
            } else {
                System.out.println("❌ Vous devez entrer exactement 9 nombres. Recommencez.");
                row--; // Recommencer cette ligne
            }
        }
        
        System.out.println("\n✅ Puzzle chargé:");
        sudoku.printGrid();
    }
}

