package org.ea.utiltities;

import org.ea.exceptions.EulerCharacteristicException;
import org.ea.exceptions.NotAClosedPolyhedronException;
import org.ea.model.Polyhedron;
import org.ea.model.Triangle;

import java.util.List;

public class PolyhedronFactory {

    public static Polyhedron buildPolyhedron(Triangle[] triangles) {
        try {
            return new Polyhedron(triangles);
        } catch (NotAClosedPolyhedronException | EulerCharacteristicException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public static Polyhedron buildPolyhedron(List<Triangle> triangles) {
        return buildPolyhedron(triangles.toArray(new Triangle[0]));
    }

}
