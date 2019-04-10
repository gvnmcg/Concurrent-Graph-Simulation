/**
 * Coordinate class used to map nodes to locations
 */
public class Coordinate {

    // Private variables
    private int x;
    private int y;


    /**
     * Coordinate that takes in two integer values, giving locations
     *
     * @param x x-Coordinate
     * @param y y-Coordinate
     */
    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Alternative creation of the Coordinate using the toString
     * implementation of a different coordinate
     *
     * @param str toString implementation of other Coordinate
     */
    Coordinate(String str) {

        String[] strArr = str.split(" ");

        this.x = Integer.parseInt(strArr[0]);
        this.y = Integer.parseInt(strArr[1]);
    }


    /**
     * Get the X coordinate
     *
     * @return x-Coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get the Y coordinate
     *
     * @return y-Coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Overwrite the toString implementation for better viewing
     *
     * return String of location
     */
    @Override
    public String toString() {
        return x+" "+y ;
    }

    /**
     * Equals overwrite checking for equality
     *
     * @param obj Object to check
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        Coordinate coord = (Coordinate) obj;
        if (coord != null && x == coord.getX() && y == coord.getY()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * For simplicity with the Map, the hashcode of the to String method will
     * give the best results
     *
     * @return integer value of the hash
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
