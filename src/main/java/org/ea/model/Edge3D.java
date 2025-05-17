package org.ea.model;

public interface Edge3D {
    Vertex3D getStart();
    Vertex3D getEnd();
    Vector3D getDirection();
    double getLength();
    default boolean equals(Edge3D other) {
        return getStart().equals(other.getStart()) && getEnd().equals(other.getEnd());
    }
}
