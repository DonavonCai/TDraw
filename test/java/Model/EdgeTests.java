package Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTests {

    @DisplayName("Set Type")
    @Test
    void setType() {
        Edge edge = new Edge();
        edge.SetType(Edge.Type.NEG);
        assertEquals(edge.GetType(), Edge.Type.NEG);
    }

    @DisplayName("Set Coord")
    @Test
    void setCoord() {
        Edge edge = new Edge();
        edge.SetCoord(100);
        assertEquals(edge.GetCoord(), 100);
    }
}