// ==================== ÉTAPE 1: Classe principale Sudoku ====================
// Cette classe contient la grille et toute la logique du jeu

import java.util.*;

public class Sudoku {
    // Constante pour la taille de la grille (9x9)
    private static final int GRID_SIZE = 9;
    // Constante pour la taille des sous-grilles (3x3)
    private static final int SUB_GRID_SIZE = 3;
    
    // La grille de jeu - tableau 2D d'entiers
    // 0 représente une case vide, 1-9 représentent les chiffres placés
    private int[][] grid;
    
    // Constructeur - initialise une grille vide
    public Sudoku() {
        this.grid = new int[GRID_SIZE][GRID_SIZE];
    }
    
    // ==================== ÉTAPE 2: Méthodes de validation ====================
    
    /**
     * Vérifie si un nombre peut être placé à une position donnée
     * @param row ligne (0-8)
     * @param col colonne (0-8)
     * @param number nombre à placer (1-9)
     * @return true si le placement est valide, false sinon
     */
    public boolean isValidPlacement(int row, int col, int number) {
        // Vérification des trois règles du Sudoku
        return !isNumberInRow(row, number) && 
               !isNumberInColumn(col, number) && 
               !isNumberInSubGrid(row, col, number);
    }
    
    /**
     * Vérifie si un nombre existe déjà dans une ligne
     */
    private boolean isNumberInRow(int row, int number) {
        for (int col = 0; col < GRID_SIZE; col++) {
            if (grid[row][col] == number) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Vérifie si un nombre existe déjà dans une colonne
     */
    private boolean isNumberInColumn(int col, int number) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (grid[row][col] == number) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Vérifie si un nombre existe déjà dans la sous-grille 3x3
     */
    private boolean isNumberInSubGrid(int row, int col, int number) {
        // Calcul des coordonnées du coin supérieur gauche de la sous-grille
        int subGridRowStart = row - row % SUB_GRID_SIZE;
        int subGridColStart = col - col % SUB_GRID_SIZE;
        
        // Parcours de la sous-grille 3x3
        for (int r = subGridRowStart; r < subGridRowStart + SUB_GRID_SIZE; r++) {
            for (int c = subGridColStart; c < subGridColStart + SUB_GRID_SIZE; c++) {
                if (grid[r][c] == number) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // ==================== ÉTAPE 3: Algorithme de résolution (Backtracking) ====================
    
    /**
     * Résout le Sudoku en utilisant l'algorithme de backtracking
     * @return true si une solution existe, false sinon
     */
    public boolean solveSudoku() {
        // Parcours de toute la grille
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                // Si on trouve une case vide (0)
                if (grid[row][col] == 0) {
                    // Essayer tous les nombres de 1 à 9
                    for (int numberToTry = 1; numberToTry <= GRID_SIZE; numberToTry++) {
                        // Si le nombre peut être placé à cette position
                        if (isValidPlacement(row, col, numberToTry)) {
                            // Placer le nombre
                            grid[row][col] = numberToTry;
                            
                            // Récursion: essayer de résoudre le reste
                            if (solveSudoku()) {
                                return true; // Solution trouvée
                            } else {
                                // Backtrack: annuler le placement si ça ne mène pas à une solution
                                grid[row][col] = 0;
                            }
                        }
                    }
                    // Si aucun nombre ne fonctionne, pas de solution
                    return false;
                }
            }
        }
        // Toutes les cases sont remplies - solution trouvée
        return true;
    }
    
    // ==================== ÉTAPE 4: Génération de grilles ====================
    
    /**
     * Génère une grille de Sudoku complètement remplie
     */
    public void generateCompleteSudoku() {
        // Vider la grille
        clearGrid();
        
        // Remplir la diagonale principale (3 sous-grilles qui ne s'influencent pas)
        fillDiagonal();
        
        // Résoudre le reste de la grille
        solveSudoku();
    }
    
    /**
     * Remplit les 3 sous-grilles de la diagonale principale
     */
    private void fillDiagonal() {
        for (int i = 0; i < GRID_SIZE; i += SUB_GRID_SIZE) {
            fillSubGrid(i, i);
        }
    }
    
    /**
     * Remplit une sous-grille 3x3 avec des nombres aléatoires
     */
    private void fillSubGrid(int row, int col) {
        Random random = new Random();
        List<Integer> numbers = new ArrayList<>();
        
        // Créer une liste des nombres 1-9
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        
        // Mélanger la liste
        Collections.shuffle(numbers);
        
        // Remplir la sous-grille
        int index = 0;
        for (int r = 0; r < SUB_GRID_SIZE; r++) {
            for (int c = 0; c < SUB_GRID_SIZE; c++) {
                grid[row + r][col + c] = numbers.get(index++);
            }
        }
    }
    
    /**
     * Crée un puzzle en retirant des nombres d'une grille complète
     * @param difficulty nombre de cases à vider (plus élevé = plus difficile)
     */
    public void createPuzzle(int difficulty) {
        generateCompleteSudoku();
        
        Random random = new Random();
        int cellsToRemove = Math.min(difficulty, 64); // Maximum 64 cases vides
        
        while (cellsToRemove > 0) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);
            
            // Si la case n'est pas déjà vide
            if (grid[row][col] != 0) {
                grid[row][col] = 0;
                cellsToRemove--;
            }
        }
    }
    
    // ==================== ÉTAPE 5: Méthodes utilitaires ====================
    
    /**
     * Vide complètement la grille
     */
    public void clearGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = 0;
            }
        }
    }
    
    /**
     * Place un nombre à une position spécifique
     */
    public boolean setNumber(int row, int col, int number) {
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
            return false; // Position invalide
        }
        
        if (number < 0 || number > 9) {
            return false; // Nombre invalide
        }
        
        if (number == 0 || isValidPlacement(row, col, number)) {
            grid[row][col] = number;
            return true;
        }
        
        return false; // Placement invalide
    }
    
    /**
     * Obtient la valeur à une position donnée
     */
    public int getNumber(int row, int col) {
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
            return -1; // Position invalide
        }
        return grid[row][col];
    }
    
    /**
     * Vérifie si la grille est complètement remplie et valide
     */
    public boolean isSolved() {
        // Vérifier qu'il n'y a pas de cases vides
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == 0) {
                    return false;
                }
            }
        }
        
        // Vérifier la validité de chaque case remplie
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int number = grid[row][col];
                grid[row][col] = 0; // Temporairement vider pour tester
                
                if (!isValidPlacement(row, col, number)) {
                    grid[row][col] = number; // Remettre la valeur
                    return false;
                }
                
                grid[row][col] = number; // Remettre la valeur
            }
        }
        
        return true;
    }
    
    /**
     * Affiche la grille dans la console avec un formatage lisible
     */
    public void printGrid() {
        System.out.println("┌───────┬───────┬───────┐");
        
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row == 3 || row == 6) {
                System.out.println("├───────┼───────┼───────┤");
            }
            
            System.out.print("│ ");
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col == 3 || col == 6) {
                    System.out.print("│ ");
                }
                
                if (grid[row][col] == 0) {
                    System.out.print(". ");
                } else {
                    System.out.print(grid[row][col] + " ");
                }
            }
            System.out.println("│");
        }
        
        System.out.println("└───────┴───────┴───────┘");
    }
    
    /**
     * Copie la grille actuelle
     */
    public int[][] copyGrid() {
        int[][] copy = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            System.arraycopy(grid[row], 0, copy[row], 0, GRID_SIZE);
        }
        return copy;
    }
    
    /**
     * Charge une grille depuis un tableau 2D
     */
    public void loadGrid(int[][] newGrid) {
        if (newGrid.length == GRID_SIZE && newGrid[0].length == GRID_SIZE) {
            for (int row = 0; row < GRID_SIZE; row++) {
                System.arraycopy(newGrid[row], 0, grid[row], 0, GRID_SIZE);
            }
        }
    }
}

// ==================== ÉTAPE 6: Interface utilisateur console ====================

class SudokuConsoleGame {
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

