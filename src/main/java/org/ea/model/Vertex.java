package org.ea.model;

public interface Vertex {
    float getX();

    float getY();

    float getZ();

    public Vector subtract(Vertex other);

    default boolean equals(Vertex other) {
        if (other == null) return false;
        return getX() == other.getX() && getY() == other.getY() && getZ() == other.getZ();
    }
}
