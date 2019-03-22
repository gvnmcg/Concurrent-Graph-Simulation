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

    @Override
    public boolean equals(Object obj) {
        Coordinate coord = (Coordinate) obj;

        if (coord != null && x == coord.getX() && y == coord.getY()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equals(Coordinate coord) {
        if (x == coord.getX() && y == coord.getY()) return true;
        else return false;
    }
}
