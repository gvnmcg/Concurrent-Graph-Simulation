import java.util.ArrayList;

public class GraphGen {

    private static final int NUM_TO_GEN = 40;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;


    private static ArrayList<Coordinate> list = new ArrayList<>();

    public static void main(String[] args) {

        for (int i = 0; i < NUM_TO_GEN; i++) {
            int w = (int)(Math.random() * WIDTH);
            int h = (int)(Math.random() * WIDTH);

            Coordinate coord = new Coordinate(w,h);

            if (!list.contains(coord)) {
                list.add(coord);
            }
        }

        for (Coordinate coord : list) {
            System.out.println("node " + coord);
        }

        for (Coordinate coord : list) {
            int distance = 50;
            Coordinate check = null;

            for (Coordinate coordToCheck : list) {
//                System.out.println(calcDistance(coord, coordToCheck));
//                if (coord == coordToCheck) continue;
                int distCheck = calcDistance(coord, coordToCheck);
                if (distCheck != 0 && distance > distCheck) {
                    distance = distCheck;
                    check = coordToCheck;
                }

            }
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
