package org.ea.controller;

import org.ea.model.Polyhedron;
import org.ea.model.Triangle;

import java.util.Arrays;

public class PolyhedronController {
    private final Polyhedron polyhedron;

    public PolyhedronController(Polyhedron polyhedron) {
        this.polyhedron = polyhedron;
    }

    public Triangle[] getSortedSurfaces() {
        Triangle[] triangles = Arrays.copyOf(this.getPolyhedron().getSurfaces(), this.getPolyhedron().getSurfaces().length);
        Arrays.sort(triangles);
        return triangles;
    }

    public Polyhedron getPolyhedron() {
        return polyhedron;
    }
}
