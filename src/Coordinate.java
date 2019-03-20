public class Coordinate {

    private int x;
    private int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
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

    public boolean equals(Coordinate coord) {
        if (x == coord.getX() && y == coord.getY()) return true;
        else return false;
    }
}
