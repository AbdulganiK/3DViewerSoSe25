package org.ea.utiltities;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.ea.model.Triangle;
import org.ea.model.Vertex;

import java.util.*;

public final class MeshFactory {

    /** Öffentliche Fassade: liefert direkt ein gerendertes {@link MeshView}. */
    public MeshView buildMeshView(Triangle[] triangles) {
        return new MeshView(buildTriangleMesh(triangles));
    }

    /** Kernmethode: erstellt nur das Mesh-Objekt. */
    public TriangleMesh buildTriangleMesh(Triangle[] triangles) {
        Objects.requireNonNull(triangles, "triangles");

        // 1 Alle eindeutigen Vertex-Positionen sammeln
        Map<Vertex, Integer> vertexIndex = buildVertexIndex(triangles);
        float[] pointArray = buildPointArray(vertexIndex.keySet());

        // 2 Face-Array anlegen (6 ints pro Dreieck: v0/t0, v1/t1, v2/t2)
        int[] faceArray = buildFaceArray(triangles, vertexIndex);

        // 3 TriangleMesh initialisieren
        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(pointArray);
        mesh.getTexCoords().addAll(0, 0);          // Dummy-TexCoord
        mesh.getFaces().addAll(faceArray);

        return mesh;
    }


    /**
     * Erstellt eine geordnete Map ⟶ Index, um Vertex-Duplikate zu vermeiden.
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
     * Wandelt die Vertex-Menge in das von JavaFX erwartete float[] um.
     * Reihenfolge bleibt erhalten (LinkedHashMap!).
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
     * Baut das Face-Array: 3 Vertex-Indizes × (VertexIndex, TexCoordIndex).
     * TexCoordIndex ist immer 0, da keine Textur verwendet wird.
     */
    private static int[] buildFaceArray(Triangle[] triangles,
                                        Map<Vertex, Integer> vertexIndex) {

        int[] faces = new int[triangles.length * 6];  // 6 ints pro Dreieck
        int f = 0;

        for (Triangle t : triangles) {
            for (Vertex v : t.getVertices()) {
                faces[f++] = vertexIndex.get(v);  // Vertex-Index
                faces[f++] = 0;                   // TexCoord-Index (Dummy)
            }
        }
        return faces;
    }
}
