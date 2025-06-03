package org.ea.model;

/**
 * Represents a 3D edge defined by two vertices.
 */
public interface Edge3D {

    /**
     * Returns the starting vertex of the edge.
     *
     * @return the start vertex
     * @precondition none
     * @postcondition Returns a non-null {@link Vertex3D} representing the start of the edge
     */
    Vertex3D getStart();

    /**
     * Returns the ending vertex of the edge.
     *
     * @return the end vertex
     * @precondition none
     * @postcondition Returns a non-null {@link Vertex3D} representing the end of the edge
     */
    Vertex3D getEnd();

    /**
     * Returns the direction vector from the start vertex to the end vertex.
     *
     * @return a {@link Vector3D} representing the direction from start to end
     * @precondition {@code getStart() != null && getEnd() != null}
     * @postcondition Returns a new {@link Vector3D} such that {@code direction = start - end}
     */
    Vector3D getDirection();

    /**
     * Returns the length of the edge.
     *
     * @return the Euclidean distance between the start and end vertices
     * @precondition {@code getDirection() != null}
     * @postcondition Returns a non-negative double representing the length of the edge
     */
    double getLength();

    /**
     * Compares this edge to another edge for equality, treating the edge as undirected.
     *
     * @param other the other edge to compare with
     * @return {@code true} if both edges have the same start and end vertices, regardless of order
     * @precondition {@code other != null}
     * @postcondition Returns {@code true} if the edges represent the same connection in space, otherwise {@code false}
     */
    default boolean equals(Edge3D other) {
        if (other == null) return false;
        return (getStart().equals(other.getStart()) && getEnd().equals(other.getEnd())) ||
                (getStart().equals(other.getEnd()) && getEnd().equals(other.getStart()));
    }
}
