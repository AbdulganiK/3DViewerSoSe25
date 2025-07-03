package org.ea.model;

/**
 * Represents a 3D solid geometry that provides surface area and volume calculations.
 *
 * @precondition The implementing class must provide meaningful implementations for area and volume.
 * @postcondition Surface area and volume can be queried using {@code getArea()} and {@code getVolume()}.
 */
public interface SolidGeometry extends Geometry {
    double getArea(); // Oberfläche
    double getVolume();
}
