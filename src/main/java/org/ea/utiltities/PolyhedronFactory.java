package org.ea.utiltities;

import org.ea.constant.GeometricConstants;
import org.ea.exceptions.EulerCharacteristicException;
import org.ea.exceptions.NotAClosedPolyhedronException;
import org.ea.model.Polygon;
import org.ea.model.Polyhedron;
import org.ea.model.Triangle;
import org.ea.model.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PolyhedronFactory implements Runnable {

    BlockingQueue<Triangle> triangleQueue;
    double area;

    public PolyhedronFactory() {
    }

    public PolyhedronFactory(BlockingQueue<Triangle> triangleQueue) {
        this.triangleQueue = triangleQueue;
    }

    public Polyhedron buildPolyhedron(Triangle[] triangles) {
        try {
            return new Polyhedron(triangles, this.calculateArea(triangles), this.calculateVolume(triangles));
        } catch (NotAClosedPolyhedronException | EulerCharacteristicException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public Polyhedron buildPolyhedron(List<Triangle> triangles) {
        return buildPolyhedron(triangles.toArray(new Triangle[0]));
    }


    public double calculateVolume(Triangle[] surfaces) {
        float volume = 0f;
        for (Polygon surface : surfaces) {
            Vector originA = surface.getVertices()[GeometricConstants.FIRST_EDGE].subtract(GeometricConstants.ORIGIN);
            Vector originB = surface.getVertices()[GeometricConstants.SECOND_EDGE].subtract(GeometricConstants.ORIGIN);
            Vector originC = surface.getVertices()[GeometricConstants.THIRD_EDGE].subtract(GeometricConstants.ORIGIN);
            volume += (float) 1 / 6 * originA.dotProduct(originB.crossProduct(originC));
        }
        return Math.abs(volume);
    }

    public double calculateArea(Triangle[] surfaces) {
        double area = 0;
        for (Triangle surface : surfaces) {
            area += surface.getArea();
        }
        return area;
    }

    public double getArea() {
        return area;
    }

    @Override
    public void run() {
        if (this.triangleQueue == null) {
            return; // Queue ist null → Abbruch
        }
        double area = 0;
        try {
            Triangle triangle = this.triangleQueue.take(); // Ein Element holen
            while (triangle.getArea() != null) {
                area += triangle.getArea();
                triangle = this.triangleQueue.take();
            }
            this.area = area;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Interrupt-Flag setzen
        }
    }
}
