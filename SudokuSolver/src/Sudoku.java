import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * file name: Sudoku.java
 * Sudoku class implementation
 *  
 * @author Akanksha Malik - Johnny Khalil - Marko Mihailovic - Jose Romero
 * @since Nov 6th, 2017
 */
public class Sudoku {
	public static final int ROWS = 9;
	public static final int COLUMNS = 9;
	public static Variable[][] grid;

	/**
	 * Sudoku Puzzle constructor; 
	 * initializes a sudoku puzzle with an empty 9x9 grid
	 */
	public Sudoku() {
		grid = new Variable[ROWS][COLUMNS];
	}

	/**
	 * Opens input file and reads its content.
	 * Populates the sudoku puzzle gird with available values,
	 * 0 indicates an empty unit.
	 * 
	 * @param fname - name of the input file
	 */
	public void populateGrid(String fname) {
		// try open file as input stream
		try (InputStream is = Files.newInputStream(Paths.get(fname));
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

			String line = null;
			int row = 0;

			// read every line in the file
			while ((line = br.readLine()) != null) {
				for (int col = 0; col < line.length(); col++) {
					// parse number as integer
					int value = Integer.parseInt(line.charAt(col) + "");
					if (value == 0) {
						grid[row][col] = new Variable(row, col, value);
					} else {
						grid[row][col] = new Variable(row, col, value, 
								new ArrayList<Integer>(Arrays.asList(value)));
					}
				}
				row++;
			}

			// if file can't be opened or not found
		} catch (IOException e) {
			System.err.println(e);
			System.exit(0); 
		}
	}

	/**
	 * Update the state of the grid after AC3 algorithm is applied.
	 * this method maps the domain of each variable to its value, iff
	 * its domain length is 1, hence the value is that item in domain
	 */
	public void updateGrid() {
		for (int row = 0; row < Sudoku.ROWS; row++ ) {
			for (int col = 0; col < Sudoku.COLUMNS; col++) {
				Variable var = Sudoku.grid[row][col];
				if (!var.isFound() && var.domain.size() == 1) {
					var.setValue(var.domain.get(0)); // only one value available in domain
				}
			}
		}
	}

	/**
	 * A csp sudoku puzzle is solved if for each Xi, Di == 1
	 * and value != 0
	 * 
	 * @return true if puzzle is solved, false otherwise
	 */
	public boolean isSolved() {
		boolean solved = true;
		for (int row = 0; row < Sudoku.ROWS; row++ ) {
			for (int col = 0; col < Sudoku.COLUMNS; col++) {
				Variable var = Sudoku.grid[row][col];
				if (!var.isFound()) {
					return false;
				}
			}
		}		
		return solved;
	}

	/**
	 * Check if assignment is complete
	 * Assignment is complete if all squares have assigned values that are consistent with
	 * original gird.
	 * 
	 * @param assignment - an assignment to be checked
	 * @return true if assignment is complete, false otherwise
	 */
	public boolean isComplete(Variable[][] assignment) {
		for (int row = 0; row < Sudoku.ROWS; row++) {
			for (int col = 0; col < Sudoku.COLUMNS; col++) {
				if ((assignment[row][col].value == 0 && grid[row][col].value == 0))
					return false;
			}
		}
		return true;
	}

	/**
	 * Check if variable's value is consistent with assignment.
	 * A variable's value is consistent with assignment if it is legal,
	 * i.e. it does not conflict with the values of neighbours.
	 * 
	 * @param var - variable whose value to be checked for consistency with assignment
	 * @param assignment - assignment that value will be checked for consistency against
	 * @return true if variable's value is consistent with assignment
	 */
	public boolean isConsistent(Variable var, Variable[][] assignment) {
		for (Constraint constraint : var.constraints) {
			if ( assignment[constraint.row][constraint.col].value == var.value
					|| grid[constraint.row][constraint.col].value == var.value) {   
				return false;   
			}
		}
		return true;
	}

	/**
	 * return the neighbours of a variable
	 * a neighbour is a variable that forms an arc this variable
	 * 
	 * @param X - variable whose neighbours to be found
	 * @return - ArrayList of neighbours of X
	 */
	public ArrayList<Variable> getNeighbours(Variable X) {
		ArrayList<Variable> neighbours = new ArrayList<Variable>();
		for (int i = 0; i < X.arcs.size(); i++) {
			neighbours.add(X.arcs.get(i).Y);
		}
		return neighbours;
	}
	
	/**
	 * Initialize binary constraints of all variables.
	 * i.e. square (0,0) has 20 binary constraints as follows:
	 * 	row = (0,1), (0,2) ... (0,8)
	 * 	col = (1,0), (2,0) ... (8,0)
	 * 	box = (1,1), (1,2), (2,1), (2,2)
	 */
	public void initConstraints() {

		for (int i = 0; i < Sudoku.ROWS; i++) {
			for (int j = 0; j < Sudoku.COLUMNS; j++) {
				// set row constraints
				for (int row = 0; row < Sudoku.ROWS; row++) {
					if (row != j)
						Sudoku.grid[i][j].constraints.add(new Constraint(i, row));
				}
				// set column constraints
				for (int col = 0; col < Sudoku.COLUMNS; ++col) {
					if (col != i)
						Sudoku.grid[i][j].constraints.add(new Constraint(col, j));
				}

				// set box constraints
				int top_row = (i/3) * 3;
				int bottom_row = top_row + 3;
				int left_col = (j/3) * 3;
				int right_col = left_col + 3;
				for (int row_unit = top_row; row_unit < bottom_row; row_unit++) {
					for (int col_unit = left_col; col_unit < right_col; col_unit++) {
						if (row_unit != i && col_unit != j) {
							Sudoku.grid[i][j].constraints.add(new Constraint(row_unit, col_unit));
						}
					}
				}
			}
		}
	}

	/**
	 * Print the sudoku puzzle gird in a nice format
	 * 0 indicates an empty unit. 
	 */
	public void printGrid() {
		for (int row = 0; row < ROWS; row++) {
			if (row % 3 == 0 && row != 0)
				System.out.println(" |\n ------------------------");
			else if (row % 3 == 0)
				System.out.println(" -------------------------");
			else
				System.out.println(" | ");
			for (int col = 0; col < COLUMNS; col++) {
				if (col % 3 == 0)
					System.out.print(" | ");
				else
					System.out.print(" ");
				System.out.print(grid[row][col]);
			}
		}
		System.out.println(" |\n -------------------------");
	}

	/**
	 * print domains of all 81 variables
	 */
	public void printDomains() {
		System.out.println("Printing all domains");
		for (int row = 0; row < Sudoku.ROWS; row++ ) {
			for (int col = 0; col < Sudoku.COLUMNS; col++) {
				System.out.printf("Domain of row %d column %d: {", row, col);
				Sudoku.grid[row][col].printDomain();
				System.out.print("}\n");
			}
		}
	}

	/**
	 * print constraints of all 81 variables
	 */
	public void printAllConstraints() {
		System.out.println("Printing all constraints");
		for (int row = 0; row < Sudoku.ROWS; row++ ) {
			for (int col = 0; col < Sudoku.COLUMNS; col++) {
				System.out.printf("Constraints of (%d, %d): {", row, col);
				Sudoku.grid[row][col].printVariableConstraints();
				System.out.print("}\n");
			}
		}
	}
} // end of Sudoku