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

    public Polyhedron getPolyhedron() {
        return polyhedron;
    }
}
