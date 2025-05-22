package org.ea.utiltities;

import org.ea.model.Edge3D;
import org.ea.model.Polygon;
import org.ea.model.Vertex3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.IntFunction;

public interface GeometryUtils {
    static List<Vertex3D> collectVerticesFromEdges(Edge3D[] edges) {
        List<Vertex3D> vertices = new ArrayList<>();
        for (Edge3D edge : edges) {
            vertices.add(edge.getStart());
            vertices.add(edge.getEnd());
        }
        return vertices;
    }

    static List<Vertex3D> collectVerticesFromSurfaces(Polygon[] surfaces) {
        List<Vertex3D> vertices = new ArrayList<>();
        for (Polygon surface : surfaces) {
           vertices.addAll(Arrays.stream(surface.getVertices()).toList());
       }
        return vertices;
    }

    static List<Edge3D> collectEdgesFromSurfaces(Polygon[] surfaces) {
        List<Edge3D> edges = new ArrayList<>();
        for (Polygon surface : surfaces) {
            edges.addAll(Arrays.stream(surface.getEdges()).toList());
        }
        return edges;
    }

    static <T> T[] removeDuplicates(List<T> items, IntFunction<T[]> arrayConstructor) {
        LinkedHashSet<T> unique = new LinkedHashSet<>(items);
        return unique.toArray(arrayConstructor.apply(0));
    }

    static  <T> T[] removeDuplicates(T[] items, IntFunction<T[]> arrayConstructor) {
        return removeDuplicates(Arrays.asList(items), arrayConstructor);
    }


}
