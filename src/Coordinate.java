/**
 * Coordinate class used to map nodes to locations
 */
public class Coordinate {

    private int x;
    private int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Coordinate(String str) {

        String[] strArr = str.split(" ");

        this.x = Integer.parseInt(strArr[0]);
        this.y = Integer.parseInt(strArr[1]);
    }



    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }

    @Override
    public boolean equals(Object obj) {
        Coordinate coord = (Coordinate) obj;
        if (coord != null && x == coord.getX() && y == coord.getY()) {
            return true;
        } else {
            return false;
        }
    }

}
