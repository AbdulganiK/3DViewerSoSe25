package org.ea.model;

import org.ea.utiltities.GeometryUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Polyhedron implements SolidGeometry {

    private final Vertex3D[] vertices;
    private final Polygon[] surfaces;
    private final Edge3D[] edges;

    public Polyhedron(Polygon[] surfaces) {
        // collect edges and vertices
        Edge3D[] edges = GeometryUtils.collectEdgesFromSurfaces(surfaces).toArray(new Edge3D[0]);
        if (!areSurfacesConnectedToEachOtherByEdges(edges)) throw new RuntimeException("Kein geschlossenes Polygon!");
        this.surfaces = surfaces;
        // removing duplicate edges
        this.edges = GeometryUtils.removeDuplicates(edges, Edge3D[]::new);
        // remove duplicate vertices
        this.vertices = GeometryUtils.removeDuplicates(GeometryUtils.collectVerticesFromSurfaces(surfaces).toArray(new Vertex3D[0]), Vertex3D[]::new);
        // check for euler)
        if (this.vertices.length - this.edges.length + this.surfaces.length!= 2) throw new RuntimeException("Eulercharakteristik nicht erfüllt!");


    }

    private boolean areSurfacesConnectedToEachOtherByEdges(Edge3D[] edges) {
        Map<Edge3D, Long> occurrences = Arrays.stream(edges)
                .collect(Collectors.groupingBy(
                        Function.identity(),     // Gruppiere nach sich selbst
                        Collectors.counting()    // Zähle Vorkommen
                ));
        // if the edges doesnt occur twice
        // the shape cannot be a closed Polyhedron
        for (Long value : occurrences.values()) {
            if (value < 2) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double getVolume() {
        float volume = 0f;
        for (Polygon surface : surfaces) {
            Vector3D originA = surface.getVertices()[GeometricConstants.FIRST_EDGE].subtract(GeometricConstants.ORIGIN);
            Vector3D originB = surface.getVertices()[GeometricConstants.SECOND_EDGE].subtract(GeometricConstants.ORIGIN);
            Vector3D originC = surface.getVertices()[GeometricConstants.THIRD_EDGE].subtract(GeometricConstants.ORIGIN);
            volume += (float) 1/6 * originA.dotProduct(originB.crossProduct(originC));
        }
        return Math.abs(volume);
    }

    @Override
    public double getArea() {
        double area = 0;
        for (Polygon surface : this.surfaces) {
            area += surface.getArea();
        }
        return area;
    }

    @Override
    public Vector3D getPosition() {
        return null;
    }

    public Edge3D[] getEdges() {
        return edges;
    }

    public Polygon[] getSurfaces() {
        return surfaces;
    }

    public Vertex3D[] getVertices() {
        return vertices;
    }
}
