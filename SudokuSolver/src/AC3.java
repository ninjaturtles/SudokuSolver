
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * file name: AC3.java
 * AC3 algorithm implementation
 * 
 * @since Nov 6th, 2017
 */
public class AC3 {

	/**
	 * The arc-consistency algorithm AC-3.
	 * After applying AC-3, either every arc is arc-consistent, 
	 * or some variable has an empty domain, indicating that 
	 * the CSP cannot be solved.
	 * 
	 * @param sudoku csp puzzle
	 * @return false if an inconsistency is found and true otherwise
	 */
	public boolean AC3Algorithm(Sudoku sudoku){
		boolean status = true;
		// a queue of arcs, initially all the arcs in csp
		Queue<Arc> queue = getAvailableArcs(); 
		
		while (!queue.isEmpty()) {
			Arc arc = queue.remove();
			Variable X = arc.X;
			Variable Y = arc.Y;
			if (revise(sudoku, X, Y)) {
				if (X.domain.size() == 0) {
					return false;
				}
				ArrayList<Variable> neighbours = sudoku.getNeighbours(X);
				int index = neighbours.indexOf(Y);
				neighbours.remove(index);
				for (Variable K : neighbours) {
					queue.add(new Arc(K, X));
				}
			}
		}
		return status;
	}
	
	/**
	 * used in AC3 algorithm, revises the domain of a variable 
	 * relevent to another variable
	 * 
	 * @param sudoku - csp puzzle
	 * @param X	- a variable
	 * @param Y - another variable
	 * @return - true if domain of X is revised, false otherwise
	 */
	public boolean revise(Sudoku sudoku, Variable X, Variable Y) {
		boolean revised = false;

		for(int i = 0; i < X.domain.size(); i++ ) {
			int x = X.domain.get(i);
			if (Y.domain.size() == 1 && Y.domain.get(0) == x) {
				X.domain.remove(i);
				revised = true;
			}
		}
		return revised;
	}

	/**
	 * Initialize arcs between all variables.
	 * i.e. square (0,0) has 20 arcs as follows:
	 * 	row = {(0,0),(0,1)}, {(0,0),(0,2)} ... {(0,0),(0,8)}
	 * 	col = {(0,0),(1,0)}, {(0,0),(0,2)} ... {(0,0),(8,0)}
	 * 	box = {(0,0),(1,1)}, {(0,0),(1,2)}, {(0,0),(2,1)}, {(0,0),(2,2)}
	 */
	public void initializeArcs() {
		for (int i = 0; i < Sudoku.ROWS; i++) {
			for (int j = 0; j < Sudoku.COLUMNS; j++) {
				for (int k = 0; k < Sudoku.grid[i][j].constraints.size(); k++) {
					int Y_row = Sudoku.grid[i][j].constraints.get(k).row;
					int Y_col = Sudoku.grid[i][j].constraints.get(k).col;
					Arc arc = new Arc(Sudoku.grid[i][j], Sudoku.grid[Y_row][Y_col]);
					Sudoku.grid[i][j].arcs.add(arc);
				}
			}
		}
	}
	
	/**
	 * return a Queue of all initially generated arcs
	 * 
	 * @return queue - all generated arcs
	 */
	public Queue<Arc> getAvailableArcs() {
		Queue<Arc> queue = new LinkedList<Arc>();

		for (int i = 0; i < Sudoku.ROWS; ++i) {
			for (int j = 0; j < Sudoku.COLUMNS; ++j) {
				for (int k =0; k < Sudoku.grid[i][j].arcs.size(); k++) {
					queue.add(Sudoku.grid[i][j].arcs.get(k));
				}
			}
		}
		return queue;
	}

	/**
	 * print all generated arcs
	 */
	public void printAllArcs() {
		System.out.println("Printing all arcs");
		for (int i = 0; i < Sudoku.ROWS; ++i) {
			for (int j = 0; j < Sudoku.COLUMNS; ++j) {
				for (int k =0; k < Sudoku.grid[i][j].arcs.size(); k++) {
					System.out.print(Sudoku.grid[i][j].arcs.get(k) + ",");
				}
				System.out.println();
			}
		}
	}

} // end of AC3
