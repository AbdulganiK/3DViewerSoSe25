package org.ea.utiltities;

import org.ea.constant.Arguments;
import org.ea.constant.GeometricConstants;
import org.ea.constant.Messages;
import org.ea.constant.PoisonPills;
import org.ea.exceptions.GeometryException;
import org.ea.exceptions.NotAClosedPolygonException;
import org.ea.exceptions.NotATriangleException;
import org.ea.exceptions.NotEnoughEdgesForAPolygonException;
import org.ea.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
/**
 * Factory class responsible for constructing Triangle objects from raw float data.
 */
public class TriangleFactory implements Runnable {
    private BlockingQueue<List<Float>> dataQueue;
    private BlockingQueue<Triangle> triangleQueue;
    private List<Triangle> triangles = new ArrayList<>();

    /**
     * Constructs a TriangleFactory with the given queues.
     *
     * @param dataQueue      queue containing raw triangle data
     * @param triangleQueue  queue for storing generated Triangle objects
     */
    public TriangleFactory(BlockingQueue<List<Float>> dataQueue, BlockingQueue<Triangle> triangleQueue) {
        this.dataQueue = dataQueue;
        this.triangleQueue = triangleQueue;
    }

    /**
     * Default constructor.
     */
    public TriangleFactory() {
    }

    /**
     * Builds a list of Triangle objects from a flat list of floats.
     *
     * @param triangleData list of floats, where each group of 12 represents one triangle
     * @return list of constructed Triangle objects
     *
     * @precondition triangleData != null && triangleData.size() % 12 == 0
     * @postcondition returns a list of Triangle objects, size = triangleData.size() / 12
     */
    public ArrayList<Triangle> buildTriangles(List<Float> triangleData) {
        Logger.info(Messages.BUILDING_TRIANGLES_STARTED);
        Timer timer = new Timer();
        timer.start();
        ArrayList<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < triangleData.size(); i += 12) {
            List<Float> triangleValues = triangleData.subList(i, i + 12);
            Triangle triangle = this.buildTriangle(triangleValues);
            triangles.add(triangle);
        }
        timer.stop();
        Logger.info(String.format(Messages.BUILT_TRIANGLES, triangles.size()));
        Logger.info(String.format(Messages.BUILD_TIME_MESSAGE, (double) timer.getElapsedMillis()));
        return triangles;
    }

    /**
     * Builds a single Triangle object from 12 float values.
     *
     * @param triangleValues list of 12 float values (3 for normal, 9 for vertices)
     * @return a Triangle object or null if construction failed
     *
     * @precondition triangleValues != null && triangleValues.size() == 12
     * @postcondition returns a Triangle object or null if an exception occurred
     */
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
        } catch (GeometryException e) {
            Logger.error(e.getMessage());
            System.exit(Arguments.EXIT_ERROR);
        }
        return null;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    /**
     * Calculates the area of a triangle from its edges using the cross product.
     *
     * @param edges array of exactly 3 edges
     * @return area of the triangle
     *
     * @precondition edges != null && edges.length == 3
     * @postcondition returns a non-negative double representing the area
     */
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

    /**
     * Calculates the perimeter of a triangle from its edges.
     *
     * @param edges array of exactly 3 edges
     * @return perimeter of the triangle
     *
     * @precondition edges != null && edges.length == 3
     * @postcondition returns a positive double representing the perimeter
     */
    public double calculatePerimeter(Edge3D[] edges) {
        return edges[GeometricConstants.FIRST_EDGE].getLength() + edges[GeometricConstants.SECOND_EDGE].getLength() + edges[GeometricConstants.THIRD_EDGE].getLength();
    }

    /**
     * Thread run method that consumes triangle data and produces Triangle objects.
     *
     * @precondition dataQueue and triangleQueue must be initialized
     * @postcondition triangleQueue contains all built triangles and a poison pill
     */
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
        try {
            this.triangleQueue.add(new Triangle(GeometryUtils.createEdgesFromVertices(PoisonPills.VERTICES_POISON_PILL), new DefaultVector(2,2,2), null, null));
        } catch (GeometryException e) {
            Logger.error(e.getMessage());
            System.exit(Arguments.EXIT_ERROR);
        }

    }
}
