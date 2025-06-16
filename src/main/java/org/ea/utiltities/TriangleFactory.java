package org.ea.utiltities;

import org.ea.constant.GeometricConstants;
import org.ea.exceptions.NotAClosedPolygonException;
import org.ea.exceptions.NotATriangleException;
import org.ea.exceptions.NotEnoughEdgesForAPolygonException;
import org.ea.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TriangleFactory implements Runnable {
    private BlockingQueue<List<Float>> dataQueue;
    private BlockingQueue<Triangle> triangleQueue;
    private List<Triangle> triangles = new ArrayList<>();


    public TriangleFactory(BlockingQueue<List<Float>> dataQueue, BlockingQueue<Triangle> triangleQueue) {
        this.dataQueue = dataQueue;
        this.triangleQueue = triangleQueue;
    }

    public TriangleFactory() {
    }

    public ArrayList<Triangle> buildTriangles(List<Float> triangleData) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < triangleData.size(); i += 12) {
            List<Float> triangleValues = triangleData.subList(i, i + 12);
            Triangle triangle = this.buildTriangle(triangleValues);
            triangles.add(triangle);
        }
        return triangles;
    }

    public Triangle buildTriangle(List<Float> triangleValues) {
        Vector normal = null;
        List<Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < triangleValues.size(); i += 3) {
            if (i == 0) {
                normal = new DefaultVector(triangleValues.get(i), triangleValues.get(i + 1), triangleValues.get(i + 2));
            } else {
                vertices.add(new DefaultVertex(triangleValues.get(i), triangleValues.get(i + 1), triangleValues.get(i + 2)));
            }
        }
        Vertex[] vertexArray = vertices.toArray(new Vertex[0]);
        try {
            Edge3D[] edges = GeometryUtils.createEdgesFromVertices(vertexArray);
            return new Triangle(edges, normal, this.calculateArea(edges), this.calculatePerimeter(edges));
        } catch (NotATriangleException | NotAClosedPolygonException | NotEnoughEdgesForAPolygonException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public double calculateArea(Edge3D[] edges) {
        double area;

        // getting direction of edges
        Vector dir1 = edges[GeometricConstants.FIRST_EDGE].getDirection();
        Vector dir2 = edges[GeometricConstants.SECOND_EDGE].getDirection();

        // calculating crossproduct
        Vector crossProduct = dir1.crossProduct(dir2);

        // length of crossproduct equals to the area of the parallelogramm
        double parallelogramArea = crossProduct.length();

        // diving parallelogramAre by 2 to get the area of triangle
        area = parallelogramArea / GeometricConstants.HALF_OF_PARALLELOGRAM;

        return area;
    }

    public double calculatePerimeter(Edge3D[] edges) {
        return edges[GeometricConstants.FIRST_EDGE].getLength() + edges[GeometricConstants.SECOND_EDGE].getLength() + edges[GeometricConstants.THIRD_EDGE].getLength();
    }


    @Override
    public void run() {
        if (this.dataQueue == null) {
            return; // Queue ist null → Abbruch
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                List<Float> triangleData = this.dataQueue.take(); // Ein Element holen

                // Prüfe auf End-Signal (null oder leere Liste)
                if (triangleData.get(0) == null) {
                    break; // Beende die Schleife
                }

                // Erstelle das Dreieck und füge es hinzu
                Triangle triangle = this.buildTriangle(triangleData);
                if (triangle != null) {
                    this.getTriangles().add(triangle);
                    this.triangleQueue.add(triangle);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Interrupt-Flag setzen
                break; // Schleife beenden
            }
        }

        // ACHTUNG GIFTIGE PILLE
        Vertex[] vertices = {new DefaultVertex(1,1,1), new DefaultVertex(2,2,2), new DefaultVertex(3,3,3)};
        Vector normal = new DefaultVector(2,2,2);
        try {
            this.triangleQueue.add(new Triangle(GeometryUtils.createEdgesFromVertices(vertices), normal, null, null));
        } catch (NotAClosedPolygonException | NotATriangleException | NotEnoughEdgesForAPolygonException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

    }
}
