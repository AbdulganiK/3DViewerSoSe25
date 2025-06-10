package org.ea.model;

import org.ea.constant.GeometricConstants;
import org.ea.utiltities.GeometryUtils;

public class Triangle extends Polygon implements Comparable<Triangle> {
    private final Vector normal;

    public Triangle(Edge3D[] edges, Vector normal) {
        super(edges);
        if (edges.length != GeometricConstants.TRIANGLE_VERTICES_AMOUNT) {
            throw new RuntimeException("A triangle must have 3 Edges!");
        }
        this.normal = normal;
    }

    public Triangle(Vertex[] vertices, Vector normal) {
        this(GeometryUtils.createEdgesFromVertices(vertices), normal);
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

    public Vector getNormal() {
        return this.normal;
    }

    @Override
    public double getPerimeter() {
        return this.getEdges()[GeometricConstants.FIRST_EDGE].getLength() + this.getEdges()[GeometricConstants.SECOND_EDGE].getLength() + this.getEdges()[GeometricConstants.THIRD_EDGE].getLength();
    }

    @Override
    public Vector getPosition() {
        return null;
    }

    @Override
    public int compareTo(Triangle o) {
        if(this.getArea() < o.getArea())
            return -1;
        else if(o.getArea() < this.getArea())
            return 1;
        return 0;
    }
}
