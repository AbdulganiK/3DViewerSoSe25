package org.ea.model;

/**
 * A default implementation of the {@link Edge3D} interface representing a directed edge in 3D space.
 */
public class DefaultEdge implements Edge3D {
    private final Vertex startVertex;
    private final Vertex endVertex;

    /**
     * Constructs a new 3D edge from the given start and end vertices.
     *
     * @param startVertex the starting vertex of the edge
     * @param endVertex   the ending vertex of the edge
     * @precondition {@code startVertex != null && endVertex != null}
     * @postcondition A new {@code DefaultEdge3D} instance is created with the specified start and end vertices
     */
    public DefaultEdge(Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    /**
     * Returns the starting vertex of this edge.
     *
     * @return the start vertex
     * @precondition none
     * @postcondition The start vertex of the edge is returned
     */
    @Override
    public Vertex getStart() {
        return this.startVertex;
    }

    /**
     * Returns the ending vertex of this edge.
     *
     * @return the end vertex
     * @precondition none
     * @postcondition The end vertex of the edge is returned
     */
    @Override
    public Vertex getEnd() {
        return this.endVertex;
    }

    /**
     * Computes and returns the direction vector of the edge from start to end.
     *
     * @return a vector pointing from the start vertex to the end vertex
     * @precondition {@code getStart() != null && getEnd() != null}
     * @postcondition A {@code Vector3D} representing {@code start - end} is returned
     */
    @Override
    public Vector getDirection() {
        return this.getStart().subtract(this.getEnd());
    }

    /**
     * Returns the length of the edge.
     *
     * @return the Euclidean distance between start and end vertices
     * @precondition {@code getDirection() != null}
     * @postcondition A non-negative double representing the edge length is returned
     */
    @Override
    public double getLength() {
        return this.getDirection().length();
    }

    /**
     * Computes a commutative hash code based on the two vertices.
     *
     * @return a hash code such that {@code hash(A,B) == hash(B,A)}
     * @precondition {@code startVertex != null && endVertex != null}
     * @postcondition A consistent, commutative hash code is returned
     */
    @Override
    public int hashCode() {
        // commutative: A^B == B^A
        return startVertex.hashCode() ^ endVertex.hashCode();
    }

    /**
     * Compares this edge to another object for equality.
     * Uses the default {@code Edge3D.equals()} implementation.
     *
     * @param obj the object to compare with
     * @return {@code true} if the other object is an {@code Edge3D} with equal vertices (in any order)
     * @precondition {@code obj != null}
     * @postcondition {@code true} is returned if the object represents the same edge, else {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Edge3D other)) return false;
        return Edge3D.super.equals(other);
    }
}
