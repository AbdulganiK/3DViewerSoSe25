package org.ea.utiltities;

import org.ea.model.Edge3D;
import org.ea.model.Vertex3D;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public interface GeometryUtils {
    default List<Vertex3D> collectVerticesFromEdges(Edge3D[] edges) {
        List<Vertex3D> vertices = new ArrayList<>();
        for (Edge3D edge : edges) {
            vertices.add(edge.getStart());
            vertices.add(edge.getEnd());
        }
        return vertices;
    }

    default Vertex3D[] removeDuplicateVertices(List<Vertex3D> vertices) {
        LinkedHashSet<Vertex3D> uniqueVertices = new LinkedHashSet<>(vertices);
        return uniqueVertices.toArray(new Vertex3D[0]);
    }

}
