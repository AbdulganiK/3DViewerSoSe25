package org.ea.utiltities;

import org.ea.constant.Arguments;
import org.ea.constant.GeometricConstants;
import org.ea.constant.Messages;
import org.ea.exceptions.GeometryException;
import org.ea.model.Polygon;
import org.ea.model.Polyhedron;
import org.ea.model.Triangle;
import org.ea.model.Vector;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Responsible for building Polyhedron objects from Triangle data.
 */
public class PolyhedronFactory implements Runnable {

    private BlockingQueue<Triangle> triangleQueue;
    double threadedArea;

    /**
     * Default constructor.
     */
    public PolyhedronFactory() {
    }

    /**
     * Constructor with triangle queue.
     *
     * @param triangleQueue the queue to consume triangles from
     */
    public PolyhedronFactory(BlockingQueue<Triangle> triangleQueue) {
        this.triangleQueue = triangleQueue;
    }

    /**
     * Builds a Polyhedron from an array of triangles.
     *
     * @param triangles array of Triangle objects
     * @return a new Polyhedron instance
     *
     * @precondition triangles != null && triangles.length > 0
     * @postcondition returns a valid Polyhedron object or terminates the program on failure
     */
    public Polyhedron buildPolyhedron(Triangle[] triangles) {
        try {
            Timer timer = new Timer();
            timer.start();
            Logger.info(Messages.BUILDING_POLYHEDRON_STARTED);
            Polyhedron polyhedron = new Polyhedron(triangles, this.calculateArea(triangles), this.calculateVolume(triangles));
            timer.stop();
            Logger.info(Messages.BUILDING_POLYHEDRON_DONE);
            Logger.info(String.format(Messages.BUILD_TIME_MESSAGE, (double) timer.getElapsedMillis()));
            return polyhedron;
        } catch (GeometryException e) {
            Logger.error(e.getMessage());
            System.exit(Arguments.EXIT_ERROR);
        }
        return null;
    }

    /**
     * Builds a Polyhedron from a list of triangles.
     *
     * @param triangles list of Triangle objects
     * @return a new Polyhedron instance
     *
     * @precondition triangles != null && !triangles.isEmpty()
     * @postcondition returns a valid Polyhedron object
     */
    public Polyhedron buildPolyhedron(List<Triangle> triangles) {
        return buildPolyhedron(triangles.toArray(new Triangle[0]));
    }

    /**
     * Calculates the volume enclosed by the given triangle surfaces using scalar triple product.
     *
     * @param surfaces array of Triangle surfaces
     * @return the volume of the polyhedron
     *
     * @precondition surfaces != null && surfaces.length > 0
     * @postcondition returns a non-negative volume as double
     */
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

    /**
     * Calculates the total surface area from the given triangle surfaces.
     *
     * @param surfaces array of Triangle surfaces
     * @return total area
     *
     * @precondition surfaces != null
     * @postcondition returns a non-negative area value
     */
    public double calculateArea(Triangle[] surfaces) {
        double area = 0;
        for (Triangle surface : surfaces) {
            area += surface.getArea();
        }
        return area;
    }

    public double getThreadedArea() {
        return threadedArea;
    }

    /**
     * Runnable method: consumes triangles from a queue and sums their area.
     *
     * @precondition triangleQueue != null and must contain triangles ending with poison pill
     * @postcondition 'area' field is updated with the total area of consumed triangles
     */
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
            this.threadedArea = area;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Interrupt-Flag setzen
        }
    }
}
