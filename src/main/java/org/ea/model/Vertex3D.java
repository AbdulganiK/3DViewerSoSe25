package org.ea.model;

public interface Vertex3D {
    float getX();

    float getY();

    float getZ();

    public Vector3D subtract(Vertex3D other);

    default boolean equals(Vertex3D other) {
        return getX() == other.getX() && getY() == other.getY() && getZ() == other.getZ();
    }
}
