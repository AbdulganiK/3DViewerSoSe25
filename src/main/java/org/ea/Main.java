package org.ea;

import org.ea.constant.Arguments;
import org.ea.controller.PolyhedronController;
import org.ea.exceptions.STLReaderException;
import org.ea.model.Triangle;
import org.ea.utiltities.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import org.ea.view.STLViewerApplication;

/**
 * Entry point and task executor for the STL Viewer application.
 * Supports different processing tasks related to STL geometry files.
 */
public class Main extends STLViewerApplication {

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments
     *
     * @precondition args != null
     * @postcondition JavaFX application is launched via {@link Application#launch}
     */
    public static void main(String[] args) {
        STLViewerApplication.launch(args);
       }

    /**
     * Executes task 2: Parses the STL file, builds the geometry,
     * and returns an array of sorted triangles by area.
     *
     * @param fileName path to the STL file to be processed
     * @return an array of {@link Triangle} objects sorted by area (ascending)
     *
     * @precondition fileName != null && fileName points to a valid, readable STL file
     * @postcondition returns a sorted array of triangles or exits the program on error
     */
    public static Triangle[] doTask2(String fileName) {
        try {
            return new PolyhedronController(
                    new PolyhedronFactory()
                            .buildPolyhedron(
                                    new TriangleFactory()
                                            .buildTriangles(
                                                    new STLFileReaderSelector()
                                                            .selectReader(new File(fileName))
                                                            .readTriangleData())))
                    .getSortedSurfaces();
        } catch (STLReaderException | IOException e) {
            Logger.error(e.getMessage());
            System.exit(Arguments.EXIT_ERROR);
        }
        return null;
    }

    /**
     * Executes task 3: Multithreaded processing of STL file data using producer-consumer pattern.
     * Starts multiple threads for triangle reading, parsing, and polyhedron construction.
     * Logs the final calculated area.
     *
     * @param fileName path to the STL file
     *
     * @precondition fileName != null && fileName points to a valid STL file
     * @postcondition Threads are started and joined, and the resulting area is logged
     */
    private static void doTask3(String fileName) {
        List<ManagedThread<?>> threads = new ArrayList<>(List.of(
                new ManagedThread<>(new TriangleFactory(TriangleDataQueue.getInstance(), TriangleQueue.getInstance())),
                new ManagedThread<>(new PolyhedronFactory(TriangleQueue.getInstance())),
                new ManagedThread<>(new STLFileReaderSelector().selectReader(new File(fileName), TriangleDataQueue.getInstance()))
        ));

        threads.forEach(ManagedThread::start);
        threads.forEach(ManagedThread::join);

        threads.stream()
                .map(ManagedThread::getTarget)
                .filter(t -> t instanceof PolyhedronFactory)
                .map(t -> (PolyhedronFactory) t)
                .findFirst()
                .ifPresent(factory -> Logger.info(String.valueOf(factory.getThreadedArea())));
    }
}
