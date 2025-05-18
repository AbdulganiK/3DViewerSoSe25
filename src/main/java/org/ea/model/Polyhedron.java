package org.ea.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Polyhedron implements SolidGeometry{

    private List<Vertex3D> vertices = new ArrayList<>();
    private List<Triangle> surfaces = new ArrayList<>();
    private List<Edge3D> edges = new ArrayList<>();

    public Polyhedron(List<Triangle> surfaces) {
        // collect edges and vertices
        for (Triangle surface : surfaces) {
            this.edges.addAll(surface.getEdges());
            this.vertices.addAll(surface.getVertices());
        }
        Map<Edge3D, Long> occurrences = this.edges.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),          // Gruppiere nach dem Element selbst
                        Collectors.counting()         // Zähle die Elemente in jeder Gruppe
                ));
        // if the edges doesnt occur twice
        // the shape cannot be a closed Polyhedron
        for (Long value : occurrences.values()) {
            if (value < 2) {
                throw new RuntimeException("Kein geschlossenes Polygon");
            }
        }
        this.surfaces = surfaces;
        // removing duplicate edges
        this.edges = this.edges.stream().distinct().toList();
        // remove duplicate vertices
        this.vertices = this.vertices.stream().distinct().toList();
        // check for euler
        //todo

    }

    @Override
    public double getVolume() {
        float volume = 0f;
        for (Triangle surface : surfaces) {
            volume += (float) 1 /6 * surface.getVertexA().subtract(GeometricConstants.ORIGIN).dotProduct(surface.getVertexB().subtract(GeometricConstants.ORIGIN).crossProduct(surface.getVertexC().subtract(GeometricConstants.ORIGIN)));
        }
        return volume;
    }

    @Override
    public double getArea() {
        return 0;
    }

    @Override
    public double getPerimeter() {
        return 0;
    }

    @Override
    public Vector3D getPosition() {
        return null;
    }

    @Override
    public Geometry translate(Vector3D translation) {
        return null;
    }

    @Override
    public Geometry scale(double factor) {
        return null;
    }

    @Override
    public Geometry rotate(Vector3D axis, double angle) {
        return null;
    }
}
