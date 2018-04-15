import java.util.ArrayList;
import java.util.Arrays;

/**
 * file name: Variable.java
 * Variable class implementation
 * 
 * @since Nov 6th, 2017
 */
public class Variable implements Comparable<Variable>{
	public int value;
	public int row;
	public int column;
	public ArrayList<Integer> domain;
	public ArrayList<Constraint> constraints;
	public ArrayList<Arc> arcs;

	/**
	 * Variable class constructor
	 * initializes a variable with default domain D={1,2,3,4,5,6,7,8,9} 
	 * and empty Constraint and Arc lists.
	 * 
	 * @param myRow - the row of the variable
	 * @param myCol - the column of the variable
	 * @param myValue - the value of the variable, 0 if empty square
	 */
	public Variable(int myRow, int myCol, int myValue) {
		this.row = myRow;
		this.column = myCol;
		this.value = myValue;
		this.domain = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9)); // default domain
		this.constraints = new ArrayList<Constraint>(); // initially empty, AC3 will populate
		this.arcs = new ArrayList<Arc>(); // initially empty, AC3 will populate
	}

	/**
	 * Variable class constructor
	 * initializes a pre-filled variable with empty Constraint and Arc lists.
	 * Domain = {myValue}
	 * 
	 * @param myRow - the row of the variable
	 * @param myCol - the column of the variable
	 * @param myValue - the value of the variable, 0 if empty square
	 * @param myDomain - the domain of the pre-filled variable, D={myValue}
	 */
	public Variable (int myRow, int myCol, int myValue, ArrayList<Integer> myDomain) {
		this.row = myRow;
		this.column = myCol;
		this.value = myValue;
		this.domain = myDomain;
		this.constraints = new ArrayList<Constraint>(); // initially empty, AC3 will populate
		this.arcs = new ArrayList<Arc>(); // initially empty, AC3 will populate
	}
	
	/**
	 * Set a value for this variable
	 * 
	 * @param myValue - value to be assigned to this variable
	 */
	public void setValue(int myValue) {
		this.value = myValue;
	}

	/**
	 * a variable's value is found if value != 0 and domain has
	 * only one possibility.
	 * 
	 * @return true if the value of variable (square) is found 
	 */
	public boolean isFound() {
		return value != 0 && domain.size() == 1;
	}

	@Override
	public String toString() {
		return "" + value;
	}

	public void print() {
		System.out.println("(" + row + "," + column +")");
	}
	
	@Override
	public int compareTo(Variable otherVariavle) {
		 return (this.domain.size() < otherVariavle.domain.size() ) ? -1
				 : (this.domain.size() > otherVariavle.domain.size()) ? 1:0 ;
	}

	/**
	 * print the domain of this variable
	 */
	public void printDomain() {
		for (int i = 0; i < domain.size(); i++ ) {
			if (i == (domain.size() - 1))
				System.out.print(domain.get(i));
			else
				System.out.print(domain.get(i) + ", ");
		}
	}

	/**
	 * print the constraints of this variable
	 */
	public void printVariableConstraints() {
		for (int i = 0; i < constraints.size(); i++ ) {
			if (i == (constraints.size() - 1))
				System.out.print(constraints.get(i));
			else
				System.out.print(constraints.get(i) + ", ");
		}
	}

} // end of Variable
