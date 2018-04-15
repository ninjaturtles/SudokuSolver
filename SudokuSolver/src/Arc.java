/**
 * file name: Arc.java
 * Arc class implementation
 * 
 * @since Nov 6th, 2017
 */
public class Arc {
    public Variable X;
    public Variable Y;

    /**
     * Arc constructor, initializes an arc between two variables
     * 
     * @param x	- first variable in the arc
     * @param y	- second variable in the arc
     */
    public Arc(Variable x, Variable y) {
        this.X = x;
        this.Y = y;
    }
    
    @Override
    public String toString() {
    		return "("+X.row +","+X.column+")"+ "("+Y.row +","+Y.column+")" + "||"; 
    }
}
