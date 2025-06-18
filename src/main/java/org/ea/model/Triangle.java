package org.ea.model;

import org.ea.constant.GeometricConstants;
import org.ea.exceptions.NotAClosedPolygonException;
import org.ea.exceptions.NotATriangleException;
import org.ea.exceptions.NotEnoughEdgesForAPolygonException;
import org.ea.utiltities.GeometryUtils;

public class Triangle extends Polygon implements Comparable<Triangle> {
    private final Vector normal;
    private Double area;
    private Double perimeter;

    public Triangle(Edge3D[] edges, Vector normal, Double area, Double perimeter) throws NotAClosedPolygonException, NotATriangleException, NotEnoughEdgesForAPolygonException {
        super(edges);
        if (edges.length != GeometricConstants.TRIANGLE_VERTICES_AMOUNT) {
            throw new NotATriangleException();
        }
        this.normal = normal;
        this.area = area;
        this.perimeter = perimeter;
    }

    public Triangle(Vertex[] vertices, Vector normal, double area, double perimeter) throws NotATriangleException, NotAClosedPolygonException, NotEnoughEdgesForAPolygonException {
        this(GeometryUtils.createEdgesFromVertices(vertices), normal, area, perimeter);
    }

    @Override
    public Double getArea() {
        return this.area;
    }

    public Vector getNormal() {
        return this.normal;
    }

    @Override
    public Double getPerimeter() {
        return this.perimeter;
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

    @Override
    public String toString() {
        return this.getArea().toString();
    }
}
