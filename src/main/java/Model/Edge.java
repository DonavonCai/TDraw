package Model;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    public enum Type {
        POS,
        NEG;

        public Type opposite() {
            if (this == POS)
                return NEG;
            else return POS;
        }
    }

    private Type type;
    private int coord;

    // Constructors: ----------------------------------------
    public Edge() {}

    public Edge(int c) {
        coord = c;
    }

    public Edge(int c, Type t) {
        coord = c;
        type = t;
    }

    public Edge(Edge e) {
        type = e.type;
        coord = e.coord;
    }

    public Edge(Type t,  int c) {
        type = t;
        coord = c;
    }

    // Accessors: -------------------------------------------
    public void SetType(Type t) {type = t;}

    public Type GetType() {return type;}

    public void SetCoord(int c) {coord = c;}

    public int GetCoord() {return coord;}

    // Overrides: -------------------------------------------
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Edge) {
            return this.type == ((Edge) obj).type &&
                   this.coord == ((Edge) obj).coord;
        }
        return false;
    }

    @Override
    public int compareTo(Edge other) {
        Integer a = Integer.valueOf(coord);
        Integer b = Integer.valueOf(other.GetCoord());
        return a.compareTo(b);
    }
}
