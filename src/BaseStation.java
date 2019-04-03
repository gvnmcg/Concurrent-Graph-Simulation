/**
 * Produces first mobile agent, has log of agents and fires
 */
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
