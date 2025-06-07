package org.ea.model;

import java.util.Objects;

/**
 * A default implementation of a vertex in 3D space.
 */
public class DefaultVertex implements Vertex {
    private float x;
    private float y;
    private float z;

    /**
     * Constructs a new vertex at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @precondition none
     * @postcondition A new {@code DefaultVertex3D} is created with the given coordinates
     */
    public DefaultVertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x-coordinate of this vertex.
     *
     * @return the x-coordinate
     * @precondition none
     * @postcondition The x-coordinate is returned
     */
    @Override
    public float getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate of this vertex.
     *
     * @return the y-coordinate
     * @precondition none
     * @postcondition The y-coordinate is returned
     */
    @Override
    public float getY() {
        return this.y;
    }

    /**
     * Returns the z-coordinate of this vertex.
     *
     * @return the z-coordinate
     * @precondition none
     * @postcondition The z-coordinate is returned
     */
    @Override
    public float getZ() {
        return this.z;
    }

    /**
     * Subtracts another vertex from this vertex and returns the resulting vector.
     *
     * @param other the vertex to subtract
     * @return a {@code Vector3D} representing the difference {@code this - other}
     * @precondition {@code other != null}
     * @postcondition A new vector is returned pointing from {@code other} to {@code this}
     */
    @Override
    public Vector subtract(Vertex other) {
        return new DefaultVector(
                this.getX() - other.getX(),
                this.getY() - other.getY(),
                this.getZ() - other.getZ()
        );
    }

    /**
     * Computes a hash code based on the x, y, and z coordinates.
     *
     * @return a consistent hash code
     * @precondition none
     * @postcondition Returns a hash code uniquely identifying this vertex by its coordinates
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    /**
     * Compares this vertex to another object for equality.
     *
     * @param obj the object to compare to
     * @return {@code true} if the other object is a {@code Vertex3D} with the same coordinates
     * @precondition {@code obj != null}
     * @postcondition Returns {@code true} if {@code obj} has equal coordinates, otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vertex other)) return false;
        return Vertex.super.equals(other);
    }
}
