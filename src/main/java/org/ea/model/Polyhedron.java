package org.ea.model;

import java.util.List;
import java.util.Set;

public class Polyhedron implements SolidGeometry{

    private List<Vertex3D> vertices;
    private List<AbstractPolygon> surfaces;
    private List<Edge3D> edges;

    public  Polyhedron(List<AbstractPolygon> surfaces) {

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
