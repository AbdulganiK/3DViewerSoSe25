package org.ea.model;

import org.ea.constant.GeometricConstants;

/**
 * Represents a 3D polygon, which is a closed polyline with at least a minimum number of edges.
 */
public abstract class Polygon extends Polyline implements SurfaceGeometry {

    /**
     * Constructs a {@code Polygon} from an array of edges.
     * The polygon must be closed (first and last vertices are connected) and must contain
     * at least the minimum number of edges defined in {@link GeometricConstants#MINIMUM_AMOUNT_OF_EDGES}.
     *
     * @param edges the edges that make up the polygon
     * @throws RuntimeException if the number of edges is too small or if the polygon is not closed
     * @precondition {@code edges != null && edges.length >= 1 && all edges != null}
     * @postcondition A new {@code Polygon} is constructed if the conditions are valid, otherwise an exception is thrown
     */
    public Polygon(Edge3D[] edges) {
        super(edges);
        if (edges.length < GeometricConstants.MINIMUM_AMOUNT_OF_EDGES) {
            throw new RuntimeException("Polygon must have at least " + GeometricConstants.MINIMUM_AMOUNT_OF_EDGES + " edges.");
        }
        if (!isPolylineClosed(this)) {
            throw new RuntimeException("Polygon must be closed (first and last points must connect).");
        }
    }

    /**
     * Checks whether the polyline is closed by comparing the first and last edges.
     *
     * @param polyline the polyline to check
     * @return {@code true} if the polyline is closed (i.e., first and last vertices are equal), otherwise {@code false}
     * @precondition {@code polyline != null && polyline.getEdges().length > 0}
     * @postcondition Returns {@code true} if polyline is closed, {@code false} otherwise
     */
    private boolean isPolylineClosed(Polyline polyline) {
        if (polyline == null || polyline.getEdges() == null || polyline.getEdges().length == 0) {
            return false; // Keine Kanten → nicht geschlossen
        }

        Edge3D[] edges = polyline.getEdges();
        Vertex firstVertex = edges[0].getStart(); // Startpunkt der ersten Kante
        Vertex lastVertex = edges[edges.length - 1].getEnd(); // Endpunkt der letzten Kante

        return firstVertex.equals(lastVertex); // Geschlossen, wenn Start und Ende gleich sind
    }
}
