package org.ea.model;
import org.ea.utiltities.GeometryUtils;


import java.util.LinkedHashSet;

public class Polyline {
    private final Edge3D[] edges;
    private final Vertex3D[] vertices;

    public Polyline(Edge3D[] edges) {
        if (!areEdgesConnected(edges)) throw new RuntimeException(ExceptionMessages.EDGES_NOT_CONNECTED);
        this.edges = edges;
        this.vertices = GeometryUtils.removeDuplicates(GeometryUtils.collectVerticesFromEdges(edges), Vertex3D[]::new);
    }

    private boolean areEdgesConnected(Edge3D[] edges) {
        for (int i = 0; i < edges.length; i++) {
            if (i + 1 != edges.length) {
                Edge3D currentEdge = edges[i];
                Edge3D nextEdge = edges[i + 1];
                if (!currentEdge.getEnd().equals(nextEdge.getStart())) {
                    return false;
                }
            }
        }
        return true;
    }

    public Edge3D[] getEdges() {
        return edges;
    }

    public Vertex3D[] getVertices() {
        return this.vertices;
    }
}
