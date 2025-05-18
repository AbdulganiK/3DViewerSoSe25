package org.ea.model;


import javafx.print.Collation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractPolygon implements SurfaceGeometry {
    List<Edge3D> edges;
    List<Vertex3D> vertices;
    public AbstractPolygon(List<Edge3D> edges) {
        if (edges.size() < 3) {
            throw new RuntimeException();
        }
        // Check connectivity between edges
        for (int i = 0; i < edges.size(); i++) {
            Edge3D currentEdge = edges.get(i);
            Edge3D nextEdge = edges.get((i + 1) % edges.size()); // Wraps around for the last edge

            if (!currentEdge.getEnd().equals(nextEdge.getStart())) {
                throw new RuntimeException("Edges are not connected properly.");
            }
        }
        this.edges = edges;
    }

    public List<Vertex3D> getVertices() {
        return vertices;
    }

    public List<Edge3D> getEdges() {
        return edges;
    }
}
