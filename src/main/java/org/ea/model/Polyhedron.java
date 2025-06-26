package org.ea.model;

import org.ea.constant.GeometricConstants;
import org.ea.constant.Numbers;
import org.ea.exceptions.EulerCharacteristicException;
import org.ea.exceptions.NotAClosedPolyhedronException;
import org.ea.utiltities.GeometryUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Polyhedron implements SolidGeometry {

    private final Vertex[] vertices;
    private final Triangle[] surfaces;
    private final Edge3D[] edges;
    private final double area;
    private final double volume;

    public Polyhedron(Triangle[] surfaces, double area, double volume) throws NotAClosedPolyhedronException, EulerCharacteristicException {
        // collect edges and vertices
        Edge3D[] edges = GeometryUtils.collectEdgesFromSurfaces(surfaces).toArray(new Edge3D[0]);
        if (!areSurfacesConnectedToEachOtherByEdges(edges)) throw new NotAClosedPolyhedronException();
        this.surfaces = surfaces;
        this.area = area;
        this.volume = volume;
        // removing duplicate edges
        this.edges = GeometryUtils.removeDuplicates(edges, Edge3D[]::new);
        // remove duplicate vertices
        this.vertices = GeometryUtils.removeDuplicates(GeometryUtils.collectVerticesFromSurfaces(surfaces).toArray(new Vertex[0]), Vertex[]::new);
        // check for euler
        //if (this.vertices.length - this.edges.length + this.surfaces.length!= Numbers.EULER_RESULT) throw new EulerCharacteristicException();

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
        return this.volume;
    }

    @Override
    public double getArea() {
            return this.area;
    }

    @Override
    public Vector getPosition() {
        return null;
    }

    public Edge3D[] getEdges() {
        return edges;
    }

    public Triangle[] getSurfaces() {
        return surfaces;
    }

    public Vertex[] getVertices() {
        return vertices;
    }
}
