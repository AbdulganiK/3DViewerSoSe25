package org.ea.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Polyhedron implements SolidGeometry{

    private List<Vertex3D> vertices = new ArrayList<>();
    private List<AbstractPolygon> surfaces = new ArrayList<>();
    private List<Edge3D> edges = new ArrayList<>();

    public Polyhedron(List<AbstractPolygon> surfaces) {
        // collect edges
        for (AbstractPolygon surface : surfaces) {
            this.edges.addAll(surface.getEdges());
        }
        Map<Edge3D, Long> occurrences = this.edges.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),          // Gruppiere nach dem Element selbst
                        Collectors.counting()         // Zähle die Elemente in jeder Gruppe
                ));

        for (Long value : occurrences.values()) {
            if (value < 2) {
                throw new RuntimeException("Kein geschlossenes Polygon");
            }
        }

        // removing duplicate Edges
        this.edges = this.edges.stream().distinct().toList();
        // collect Vertices
        System.out.println(this.edges);
        // check for euler

    }

    @Override
    public double getVolume() {
        return 0;
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
