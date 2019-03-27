public class BaseStation extends GraphNode {


    private GraphNode node = null;

    BaseStation(Coordinate coordinate){
        //if extended
        super(coordinate);
    }

//    BaseStation(GraphNode node){
//        this.node = node;
//    }

    public MobileAgent initAgent(GraphNode baseStation){
        return new MobileAgent(node, false);
    }
}
