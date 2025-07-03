package org.ea.model;

/**
 * Represents a 2D surface geometry capable of reporting its area and perimeter.
 *
 * @precondition The implementing class must define valid surface characteristics.
 * @postcondition Area and perimeter values can be queried reliably.
 */
public interface SurfaceGeometry extends Geometry {
    Double getArea();
    Double getPerimeter();
}
