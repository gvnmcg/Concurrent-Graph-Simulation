import java.util.ArrayList;

/**
 * PLEASE NOTE:
 * This has no purpose in the scheme of the application but rather a way to
 * generate odd and unpredictable graphs.
 *
 */
public class GraphGen {

    private static final int NUM_TO_GEN = 30;
    private static final int SIZE = 12;
    private static ArrayList<Coordinate> list = new ArrayList<>();


    public static void main(String[] args) {

        for (int i = 0; i < NUM_TO_GEN; i++) {
            int w = (int)(Math.random() * SIZE);
            int h = (int)(Math.random() * SIZE);

            Coordinate coord = new Coordinate(w,h);

            if (!list.contains(coord)) {
                list.add(coord);
            }
        }

        for (Coordinate coord : list) {
            System.out.println("node " + coord);
        }

        ArrayList<Coordinate> coords = new ArrayList<>();

        for (Coordinate coord : list) {
            int distance = Integer.MAX_VALUE;
            Coordinate check = null;

            for (Coordinate coordToCheck : list) {

                int distCheck = calcDistance(coord, coordToCheck);

                if (coords.contains(check) && distCheck != 0 && distance > distCheck) {
                    distance = distCheck;
                    check = coordToCheck;
                }

            }
            coords.add(check);
            System.out.println("edge " + coord + " " + check);
        }

        for (Coordinate coord : list) {
            System.out.println("edge " + coord + " " + list.get((int)(Math.random() * (NUM_TO_GEN-5))));
        }

    }

    private static int calcDistance(Coordinate c1, Coordinate c2) {
        return ((int)(Math.sqrt(Math.pow(c2.getX()-c1.getX(), 2) + Math.pow(c2.getY()-c1.getY(), 2))));
    }
}
