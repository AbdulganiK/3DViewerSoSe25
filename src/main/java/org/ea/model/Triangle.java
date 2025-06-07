package org.ea.model;

public class Triangle extends Polygon {
    public Triangle(Edge3D[] edges) {
        super(edges);
        if (edges.length != GeometricConstants.TRIANGLE_VERTICES_AMOUNT) {
            throw new RuntimeException("A triangle must have 3 Vertices!");
        }

    }

    @Override
    public double getArea() {
        // getting direction of edges
        Vector dir1 = this.getEdges()[GeometricConstants.FIRST_EDGE].getDirection();
        Vector dir2 = this.getEdges()[GeometricConstants.SECOND_EDGE].getDirection();

        // calculating crossproduct
        Vector crossProduct = dir1.crossProduct(dir2);

        // length of crossproduct equals to the area of the parallelogramm
        double parallelogramArea = crossProduct.length();

        // diving parallelogramAre by 2 to get the area of triangle
        return parallelogramArea / GeometricConstants.HALF_OF_PARALLELOGRAM;
    }

    @Override
    public double getPerimeter() {
        return this.getEdges()[GeometricConstants.FIRST_EDGE].getLength() + this.getEdges()[GeometricConstants.SECOND_EDGE].getLength() + this.getEdges()[GeometricConstants.THIRD_EDGE].getLength();
    }

    @Override
    public Vector getPosition() {
        return null;
    }
}
