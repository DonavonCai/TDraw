package TimingDiagram.DSignal.Edge;

import java.io.Serializable;

public class Edge implements Serializable {
    public enum Type {POS, NEG};
    public enum Location {START, MID, END};

    private Type type;
    private Location location;
    private double coord;

    public Edge() {}

    public Edge(Type t, Location loc, double c) {
        type = t;
        location = loc;
        coord = c;
    }

    // deep copy
    public Edge(Edge copy) {
        type = copy.getType();
        location = copy.getLocation();
        coord = copy.getCoord();
    }

    public void copy(Edge e) {
        type = e.getType();
        location = e.getLocation();
        coord = e.getCoord();
    }

    public void setType(Type t) {type = t;}
    public Type getType() {return type;}

    public void setLocation(Location loc) {location = loc;}
    public Location getLocation() {return location;}

    public void setCoord(double c) {coord = c;}
    public double getCoord() {return coord;}

    public void calculateLocation(double signalWidth) {
        if (coord < 0)
            location = Location.START;

        else if (coord > signalWidth)
            location = Location.END;

        else
            location = Location.MID;
    }

    public static Type oppositeType(Type t) {
        if (t == Type.POS)
            return Type.NEG;
        else if (t == Type.NEG)
            return Type.POS;
        else
            return null;
    }
}
