package org.ea.controller;

import org.ea.constant.Messages;
import org.ea.model.Polyhedron;
import org.ea.model.Triangle;
import org.ea.utiltities.Logger;
import org.ea.utiltities.Timer;

import java.util.Arrays;

public class PolyhedronController {
    private final Polyhedron polyhedron;

    public PolyhedronController(Polyhedron polyhedron) {
        this.polyhedron = polyhedron;
    }

    /**
     * Returns the surfaces (triangles) of the polyhedron sorted by their natural order.
     *
     * @precondition
     * - getPolyhedron() must not return null.
     * - getPolyhedron().getSurfaces() must return a non-null array.
     * - All elements of the array must be non-null and implement Comparable<Triangle>.
     *
     * @postcondition
     * - The returned array contains the same Triangle objects as the original array, but sorted in ascending order.
     * - The original surfaces array remains unchanged (a defensive copy is used).
     * - The sorting duration is logged.
     * - The first five sorted elements are logged.
     *
     * @return A sorted array of Triangle objects.
     */
    public Triangle[] getSortedSurfaces() {
        Triangle[] triangles = Arrays.copyOf(this.getPolyhedron().getSurfaces(), this.getPolyhedron().getSurfaces().length);
        Timer timer = new Timer();
        timer.start();
        Logger.info(Messages.STARTED_SORTING_SURFACES);
        Arrays.sort(triangles);
        timer.stop();
        Logger.info(Messages.DONE_SORTING_SURFACES);
        Logger.info(String.format(Messages.SORTING_TIME_MESSAGE, (double) timer.getElapsedMillis()));
        Logger.logFirstFiveElements(triangles);
        return triangles;
    }

    public Double getArea() {
        return this.polyhedron.getArea();
    }

    public Double getVolume() {
        return this.polyhedron.getVolume();
    }


    public Polyhedron getPolyhedron() {
        return polyhedron;
    }
}
