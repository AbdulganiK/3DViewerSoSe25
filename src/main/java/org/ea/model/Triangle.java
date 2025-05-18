package org.ea.model;

import java.util.*;

public class Triangle extends AbstractPolygon{

    private Edge3D edgeA;
    private Edge3D edgeB;
    private Edge3D edgeC;

    private Vertex3D vertexA;
    private Vertex3D vertexB;
    private Vertex3D vertexC;

    public Triangle(List<Edge3D> edges) {
        super(edges);
        if (edges.size() != 3) {
            throw new RuntimeException();
        }
        this.edgeA = edges.getFirst();
        this.edgeB = edges.get(GeometricConstants.SECOND_EDGE);
        this.edgeC = edges.getLast();
        this.vertexA = this.edgeA.getEnd();
        this.vertexB = this.edgeB.getEnd();
        this.vertexC = this.edgeC.getEnd();
        this.getVertices().add(this.vertexA);
        this.getVertices().add(this.vertexB);
        this.getVertices().add(this.vertexC);
    }

    @Override
    public double getArea() {
        // getting direction of edges
        Vector3D dir1 = this.edgeA.getDirection();
        Vector3D dir2 = this.edgeB.getDirection();

        // calculating crossproduct
        Vector3D crossProduct = dir1.crossProduct(dir2);

        // length of crossproduct equals to the area of the parallelogramm
        double parallelogramArea = crossProduct.length();

        // diving parallelogramAre by 2 to get the area of triangle
        return parallelogramArea / GeometricConstants.HALF_OF_PARALLELOGRAM;
    }

    @Override
    public double getPerimeter() {
        return this.edgeA.getLength() + this.edgeB.getLength() + this.edgeC.getLength();
    }

    @Override
    public Vector3D getPosition() {
        return null;
    }

    public Vertex3D getVertexA() {
        return vertexA;
    }

    public Vertex3D getVertexB() {
        return vertexB;
    }

    public Vertex3D getVertexC() {
        return vertexC;
    }
}
