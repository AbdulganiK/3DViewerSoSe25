package org.ea.model;
public abstract class Polygon extends Polyline implements SurfaceGeometry {
    public Polygon(Edge3D[] edges) {
        super(edges);
        if (edges.length < GeometricConstants.MINIMUM_AMOUNT_OF_EDGES) throw new RuntimeException();
        if (!isStartOfPolyLineConnectedWithEnd(edges)) throw new RuntimeException();
    }

    private boolean isStartOfPolyLineConnectedWithEnd(Edge3D[] edges) {
        int lastEdgeIndex = edges.length - 1;
        return edges[GeometricConstants.FIRST_EDGE].equals(edges[lastEdgeIndex]);
    }

}
