package org.ea.utiltities;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.ea.model.Triangle;
import org.ea.model.Vertex;

import java.util.*;

/**
 * Utility class to construct a {@link MeshView} and its underlying {@link TriangleMesh}
 * from an array of {@link Triangle} objects.
 *
 * @precondition Input triangles must be valid and contain well-formed vertices.
 * @postcondition Returns a mesh suitable for rendering in JavaFX.
 */
public final class MeshFactory {

    /**
     * Public API: Builds and returns a {@link MeshView} from the given triangles.
     *
     * @param triangles an array of Triangle objects
     * @return a rendered MeshView object
     * @precondition {@code triangles} is not null
     * @postcondition MeshView is created and ready for rendering
     */
    public MeshView buildMeshView(Triangle[] triangles) {
        return new MeshView(buildTriangleMesh(triangles));
    }

    /**
     * Core method: Builds only the {@link TriangleMesh} without wrapping it in a MeshView.
     *
     * @param triangles array of Triangle objects
     * @return a TriangleMesh containing points and faces
     * @precondition {@code triangles} is not null
     * @postcondition A TriangleMesh is initialized with all vertices and faces
     */
    public TriangleMesh buildTriangleMesh(Triangle[] triangles) {
        Objects.requireNonNull(triangles, "triangles");

        Map<Vertex, Integer> vertexIndex = buildVertexIndex(triangles);
        float[] pointArray = buildPointArray(vertexIndex.keySet());
        int[] faceArray = buildFaceArray(triangles, vertexIndex);

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(pointArray);
        mesh.getTexCoords().addAll(0, 0);
        mesh.getFaces().addAll(faceArray);

        return mesh;
    }

    /**
     * Builds an ordered vertex-to-index map to avoid duplicate vertex storage.
     *
     * @param triangles array of Triangle objects
     * @return a LinkedHashMap of vertices to their assigned index
     * @precondition {@code triangles} is not null and contains valid vertices
     * @postcondition Map contains unique vertices mapped to indices
     */
    private static Map<Vertex, Integer> buildVertexIndex(Triangle[] triangles) {
        Map<Vertex, Integer> indexMap = new LinkedHashMap<>();
        int nextIndex = 0;

        for (Triangle t : triangles) {
            for (Vertex v : t.getVertices()) {
                if (!indexMap.containsKey(v)) {
                    indexMap.put(v, nextIndex++);
                }
            }
        }
        return indexMap;
    }

    /**
     * Converts the vertex collection into a float array as required by JavaFX.
     *
     * @param vertices collection of unique vertices
     * @return array of floats in the order x, y, z, x, y, z...
     * @precondition {@code vertices} must be a non-null collection
     * @postcondition Float array of coordinates is returned, preserving order
     */
    private static float[] buildPointArray(Collection<Vertex> vertices) {
        float[] points = new float[vertices.size() * 3];
        int i = 0;
        for (Vertex v : vertices) {
            points[i++] = v.getX();
            points[i++] = v.getY();
            points[i++] = v.getZ();
        }
        return points;
    }

    /**
     * Builds the face array for the mesh. Each triangle is represented by 3 vertex indices
     * and corresponding dummy texture indices.
     *
     * @param triangles array of triangles
     * @param vertexIndex map of vertices to indices
     * @return array of face indices
     * @precondition {@code triangles} and {@code vertexIndex} are valid and aligned
     * @postcondition Returns an array of faces referencing unique vertices
     */
    private static int[] buildFaceArray(Triangle[] triangles,
                                        Map<Vertex, Integer> vertexIndex) {

        int[] faces = new int[triangles.length * 6];
        int f = 0;

        for (Triangle t : triangles) {
            for (Vertex v : t.getVertices()) {
                faces[f++] = vertexIndex.get(v);
                faces[f++] = 0;
            }
        }
        return faces;
    }
}