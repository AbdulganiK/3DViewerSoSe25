package org.ea.model;

/**
 * Represents a point in 3D space with x, y, and z coordinates.
 *
 * @precondition Implementing classes must provide valid float coordinates.
 * @postcondition Vertex operations such as subtraction and equality check are supported.
 */
public interface Vertex {

    /**
     * @return X coordinate of the vertex
     * @precondition None
     * @postcondition A float value representing the X coordinate is returned
     */
    float getX();

    /**
     * @return Y coordinate of the vertex
     * @precondition None
     * @postcondition A float value representing the Y coordinate is returned
     */
    float getY();

    /**
     * @return Z coordinate of the vertex
     * @precondition None
     * @postcondition A float value representing the Z coordinate is returned
     */
    float getZ();

    /**
     * Returns the vector difference from another vertex to this vertex.
     *
     * @param other the vertex to subtract
     * @return a {@link Vector} representing the difference
     * @precondition {@code other} is not null
     * @postcondition A new vector from {@code other} to this vertex is returned
     */
    Vector subtract(Vertex other);

    /**
     * Checks whether this vertex is equal to another by comparing coordinates.
     *
     * @param other the other vertex
     * @return true if all coordinates match; false otherwise
     * @precondition None
     * @postcondition Equality is determined based on coordinate comparison
     */
    default boolean equals(Vertex other) {
        if (other == null) return false;
        return getX() == other.getX() && getY() == other.getY() && getZ() == other.getZ();
    }
}
