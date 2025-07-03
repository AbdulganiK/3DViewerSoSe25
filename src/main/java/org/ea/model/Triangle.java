package org.ea.model;

import org.ea.constant.GeometricConstants;
import org.ea.exceptions.NotAClosedPolygonException;
import org.ea.exceptions.NotATriangleException;
import org.ea.exceptions.NotEnoughEdgesForAPolygonException;
import org.ea.utiltities.GeometryUtils;

/**
 * Represents a triangle in 3D space as a specialized {@link Polygon}.
 * Includes normal vector, area, and perimeter.
 *
 * @precondition Requires exactly three edges or three vertices forming a closed triangle.
 * @postcondition Provides access to geometric and structural properties.
 */
public class Triangle extends Polygon implements Comparable<Triangle> {

    private final Vector normal;
    private Double area;
    private Double perimeter;

    /**
     * Constructs a triangle from edges and geometric metadata.
     *
     * @param edges the triangle's edges
     * @param normal the normal vector
     * @param area surface area of the triangle
     * @param perimeter perimeter of the triangle
     * @throws NotAClosedPolygonException if the polygon is not closed
     * @throws NotATriangleException if the edge count is not exactly 3
     * @throws NotEnoughEdgesForAPolygonException if the number of edges is less than 3
     * @precondition {@code edges.length == 3}, edges form a closed triangle
     * @postcondition Initializes internal state with valid geometry
     */
    public Triangle(Edge3D[] edges, Vector normal, Double area, Double perimeter)
            throws NotAClosedPolygonException, NotATriangleException, NotEnoughEdgesForAPolygonException {
        super(edges);
        if (edges.length != GeometricConstants.TRIANGLE_VERTICES_AMOUNT) {
            throw new NotATriangleException();
        }
        this.normal = normal;
        this.area = area;
        this.perimeter = perimeter;
    }

    /**
     * Constructs a triangle from vertices and geometric metadata.
     *
     * @param vertices triangle vertices
     * @param normal the normal vector
     * @param area precomputed area
     * @param perimeter precomputed perimeter
     * @throws NotATriangleException if vertices do not form a valid triangle
     * @throws NotAClosedPolygonException if vertices do not form a closed polygon
     * @throws NotEnoughEdgesForAPolygonException if not enough edges can be formed
     * @precondition {@code vertices.length == 3}, all vertices are valid
     * @postcondition Triangle is initialized from edges derived from the given vertices
     */
    public Triangle(Vertex[] vertices, Vector normal, double area, double perimeter)
            throws NotATriangleException, NotAClosedPolygonException, NotEnoughEdgesForAPolygonException {
        this(GeometryUtils.createEdgesFromVertices(vertices), normal, area, perimeter);
    }

    /**
     * Returns the area of the triangle.
     *
     * @return surface area
     * @precondition Triangle has valid geometry
     * @postcondition Area is returned unchanged
     */
    @Override
    public Double getArea() {
        return this.area;
    }

    /**
     * Returns the triangle's normal vector.
     *
     * @return unit vector perpendicular to the surface
     * @precondition The triangle was initialized with a normal
     * @postcondition The same normal vector is returned
     */
    public Vector getNormal() {
        return this.normal;
    }

    /**
     * Returns the perimeter of the triangle.
     *
     * @return perimeter value
     * @precondition Triangle has valid side lengths
     * @postcondition Perimeter is returned unchanged
     */
    @Override
    public Double getPerimeter() {
        return this.perimeter;
    }

    /**
     * Returns the representative position of the triangle (not implemented).
     *
     * @return null (not used in this class)
     * @precondition None
     * @postcondition Always returns {@code null}
     */
    @Override
    public Vector getPosition() {
        return null;
    }

    /**
     * Compares two triangles by surface area.
     *
     * @param o another triangle
     * @return -1 if this < o, 1 if this > o, 0 if equal
     * @precondition Both triangles are initialized and comparable
     * @postcondition Relative ordering by area is returned
     */
    @Override
    public int compareTo(Triangle o) {
        if (this.getArea() < o.getArea()) return -1;
        else if (o.getArea() < this.getArea()) return 1;
        return 0;
    }

    /**
     * Returns a string representation of the triangle's area.
     *
     * @return area as string
     * @precondition Area is not null
     * @postcondition A string form of the area is returned
     */
    @Override
    public String toString() {
        return this.getArea().toString();
    }
}
