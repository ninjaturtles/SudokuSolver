import java.util.Scanner;

/**
 * file name: Main.java
 * Main class
 * 
 * @since Nov 6th, 2017
 */
public class Main {

    public static void main(String[] args) {
    	
        if (args.length < 1) {
        		System.out.println("Must pass path to the input file.");
            return;
        }

		Sudoku sudoku = new Sudoku();

		// load file into grid
		sudoku.populateGrid(args[0]);
		// set initial binary constraints between variables
		sudoku.initConstraints();
		
		System.out.println("Sudoku Puzzle: Initial State. (0 = empty square)");
		sudoku.printGrid();	// print initial state
		
		System.out.println("============================================");
		AC3 ac3 = new AC3();
		// set initial arcs between variables
		ac3.initializeArcs();
		
		System.out.println("============================================");
		// apply AC3 algorithm to the puzzle
		boolean status = ac3.AC3Algorithm(sudoku);
		// update the grid according to the results of AC3
		sudoku.updateGrid();
		
		System.out.println("\n============================================");
		// display the solution if the puzzle is solved. otherwise an error msg
		if (status && sudoku.isSolved()) {
			System.out.println("AC3 finished with no errors");
			System.out.println("Equivalent arc-consistent puzzle found");
			System.out.println("============================================");
			System.out.println("Sudoku Puzzle: Final State.");
			sudoku.printGrid();
			
		} else {
			System.out.println("Puzzle Cannot be solved using AC3");
			System.out.println("============================================");
			System.out.println("Sudoku Puzzle: Current State. (0 = empty square)");
			sudoku.printGrid();
			//sudoku.printDomains();
			
			System.out.println("============================================");
			System.out.print("Apply BackTracking Algorithm ? (y/n)...");
			Scanner scanner = new Scanner(System.in);
			
			if (scanner.nextLine().equals("y")) {
				BackTrack backtrack = new BackTrack(sudoku);
				boolean solved = backtrack.backtrackSearch();

				if (solved) {
					System.out.println("BackTrack Algorithm finished with no errors");
					System.out.println("============================================");
					System.out.println("Sudoku Puzzle: Final State.");
					backtrack.printSolution();
				
				} else {
					System.out.println("============================================");
					System.out.println("Puzzle Cannot be solved using BackTracking");
				}
				
			} else {
				System.out.println("Program terminated.");
			}
			scanner.close();
			System.exit(0);
		}
    }
} // end of Main
