package Model;

import java.util.Objects;

public class Edge {
    public enum Type {POS, NEG};

    private Type type;
    private int coord;

    // constructors
    public Edge() {}

    public Edge(int c) {
        coord = c;
    }

    public Edge(Edge e) {
        type = e.type;
        coord = e.coord;
    }

    public Edge(Type t,  int c) {
        type = t;
        coord = c;
    }

    public void SetType(Type t) {type = t;}
    public Type GetType() {return type;}

    public void SetCoord(int c) {coord = c;}
    public int GetCoord() {return coord;}

//    @Override
//    public boolean equals(Object obj) {
//        if (obj != null && obj instanceof Edge) {
//            return Objects.equals(this, obj);
////            return this.type == ((Edge) obj).type && this.coord == ((Edge) obj).coord;
//        }
//        return false;
//    }
}
