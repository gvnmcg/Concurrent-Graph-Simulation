public class BaseStation  {


    private GraphNode node = null;

    BaseStation(Coordinate coordinate){
        //if extended
        //super(coordinate);
    }

    BaseStation(GraphNode node){
        this.node = node;
    }

    public MobileAgent initAgent(){
        return new MobileAgent(node, false);
    }
}
