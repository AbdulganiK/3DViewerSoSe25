package org.ea.utiltities;

import org.ea.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TriangleFactory {

    public static ArrayList<Triangle> buildTriangles(List<Float> triangleData) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < triangleData.size(); i+=12) {
            List<Float> triangleValues = triangleData.subList(i, i+12);
            Triangle triangle = buildTriangle(triangleValues);
            triangles.add(triangle);
        }
        return triangles;
    }

    private static Triangle buildTriangle(List<Float> triangleValues) {
        Vector normal = null;
        List<Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < triangleValues.size(); i+=3) {
            if (i == 0) {
                normal = new DefaultVector(triangleValues.get(i), triangleValues.get(i+1), triangleValues.get(i+2));
            } else {
                vertices.add(new DefaultVertex(triangleValues.get(i), triangleValues.get(i+1), triangleValues.get(i+2)));
            }
        }
        Vertex[] vertexArray = vertices.toArray(new Vertex[0]);
        return new Triangle(vertexArray, normal);
    }


}
