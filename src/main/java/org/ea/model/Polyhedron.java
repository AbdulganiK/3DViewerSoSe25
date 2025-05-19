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
        if (this.vertices.size() - this.edges.size() + this.surfaces.size() != 2) {
            throw new RuntimeException("Eulercharakteristik nicht erfüllt!");
        }

    }

    @Override
    public double getVolume() {
        float volume = 0f;
        for (Triangle surface : surfaces) {
            Vector3D originA = surface.getVertexA().subtract(GeometricConstants.ORIGIN);
            Vector3D originB = surface.getVertexB().subtract(GeometricConstants.ORIGIN);
            Vector3D originC = surface.getVertexC().subtract(GeometricConstants.ORIGIN);
            volume += (float) 1/6 * originA.dotProduct(originB.crossProduct(originC));
        }
        return Math.abs(volume);
    }

    @Override
    public double getArea() {
        double area = 0;
        for (Triangle surface : this.surfaces) {
            area += surface.getArea();
        }
        return area;
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
