package org.ea.model;
public abstract class Polygon extends Polyline implements SurfaceGeometry {
    public Polygon(Edge3D[] edges) {
        super(edges);
        if (edges.length < GeometricConstants.MINIMUM_AMOUNT_OF_EDGES) throw new RuntimeException();
        if (!isPolylineClosed(this)) throw new RuntimeException();
    }

    private boolean isPolylineClosed(Polyline polyline) {
        int lastEdgeIndex = polyline.getEdges().length - 1;
        return polyline.getEdges()[GeometricConstants.FIRST_EDGE].equals(polyline.getEdges()[lastEdgeIndex]);
    }

}
