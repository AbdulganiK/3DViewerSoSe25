package org.ea.utiltities;

import org.ea.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TriangleFactory {

    public static ArrayList<Triangle> buildTriangles(List<Float> triangleData) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < triangleData.size(); i+=12) {
            List<Float> triangleValues = triangleData.subList(i, i+11);
            Triangle triangle = buildTriangle(triangleValues);
            triangles.add(triangle);
        }
        return triangles;
    }

    private static Triangle buildTriangle(List<Float> triangleValues) {
        for (int i = 0; i < triangleValues.size(); i+=3) {

        }
    }


}
