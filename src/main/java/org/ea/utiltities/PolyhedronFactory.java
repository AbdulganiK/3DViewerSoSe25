package org.ea.utiltities;

import org.ea.model.Polyhedron;
import org.ea.model.Triangle;

import java.util.List;

public class PolyhedronFactory {

    public static Polyhedron buildPolyhedron(Triangle[] triangles) {
        return new Polyhedron(triangles);
    }

    public static Polyhedron buildPolyhedron(List<Triangle> triangles) {
        return new Polyhedron(triangles.toArray(new Triangle[0]));
    }

}
