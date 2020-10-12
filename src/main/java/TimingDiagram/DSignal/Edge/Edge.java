package TimingDiagram.DSignal.Edge;

public class Edge {
    enum Type {POSITIVE, NEGATIVE};
    enum Location {STARTING, MIDDLE, CLOSING};

    Type type;
    Location location;
    private double coord;

    Edge(Type t, Location loc, double c) {
        type = t;
        location = loc;
        coord = c;
    }

    public void setType(Type t) {type = t;}
    public void setCoord(double c) {coord = c;}
}
