package org.ea.model;

import org.ea.utiltities.GeometryUtils;

/**
 * Represents a connected sequence of 3D edges forming a polyline.
 */
public class Polyline {
    private final Edge3D[] edges;
    private final Vertex3D[] vertices;

    /**
     * Constructs a polyline from an array of connected edges.
     * All edges must be connected in sequence: the end of one edge must match the start of the next.
     *
     * @param edges an array of connected {@link Edge3D} objects
     * @throws RuntimeException if the edges are not connected
     * @precondition {@code edges != null && edges.length > 0 && all edges != null}
     * @postcondition Creates a polyline and computes a unique, ordered list of vertices
     */
    public Polyline(Edge3D[] edges) {
        if (!areEdgesConnected(edges)) {
            throw new RuntimeException(ExceptionMessages.EDGES_NOT_CONNECTED);
        }
        this.edges = edges;
        this.vertices = GeometryUtils.removeDuplicates(
                GeometryUtils.collectVerticesFromEdges(edges),
                Vertex3D[]::new
        );
    }

    /**
     * Checks whether the given edges are connected in a continuous sequence.
     *
     * @param edges the array of edges to check
     * @return {@code true} if every edge is connected to the next, otherwise {@code false}
     * @precondition {@code edges != null && edges.length > 0}
     * @postcondition Returns {@code true} if the sequence of edges is valid, {@code false} otherwise
     */
    private boolean areEdgesConnected(Edge3D[] edges) {
        for (int i = 0; i < edges.length - 1; i++) {
            Edge3D currentEdge = edges[i];
            Edge3D nextEdge = edges[i + 1];
            if (!currentEdge.getEnd().equals(nextEdge.getStart())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the array of edges forming the polyline.
     *
     * @return the edges of the polyline
     * @precondition none
     * @postcondition Returns a reference to the internal array of edges
     */
    public Edge3D[] getEdges() {
        return edges;
    }

    /**
     * Returns the array of unique vertices in the polyline, in order of appearance.
     *
     * @return the vertices of the polyline
     * @precondition none
     * @postcondition Returns a deduplicated, ordered array of vertices
     */
    public Vertex3D[] getVertices() {
        return this.vertices;
    }
}
