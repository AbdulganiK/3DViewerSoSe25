package org.ea.model;

public class Polyhedron implements SolidGeometry{
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
