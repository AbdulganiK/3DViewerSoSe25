package org.ea;

import org.ea.constant.Arguments;
import org.ea.controller.PolyhedronController;
import org.ea.exceptions.STLReaderException;
import org.ea.model.Triangle;
import org.ea.utiltities.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class Main {

    /**
     * Entry point of the program. Executes task 2 using the input file specified in the arguments.
     *
     * @precondition
     * - args must not be null and must contain a valid entry at index Arguments.FILE_NAME_ARGUMENT.
     * - args[Arguments.FILE_NAME_ARGUMENT] must be a valid path to an input file.
     *
     * @postcondition
     * - Task 2 is executed with the specified input file.
     * - Any output or logging defined in doTask2 is performed.
     *
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args){
        doTask2(args[Arguments.FILE_NAME_ARGUMENT]);
    }

    /**
     * @param fileName
     * @return sorted Triangles
     * @precondition Valid triangle file needs to exist
     * @postcondition Triangles sorted by area in increasing order
     */
    private static Triangle[] doTask2(String fileName) {

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

    private static void doTask3(String fileName){

        List<ManagedThread<?>> threads = new ArrayList<>(List.of(
                new ManagedThread<>(new TriangleFactory(TriangleDataQueue.getInstance(), TriangleQueue.getInstance())),
                new ManagedThread<>(new PolyhedronFactory(TriangleQueue.getInstance())),
                new ManagedThread<>(new STLFileReaderSelector().selectReader(new File(fileName), TriangleDataQueue.getInstance()))
        ));
        threads.forEach(ManagedThread::start);
        // Auf alle Threads warten
        threads.forEach(ManagedThread::join);
        threads.stream()
                .map(ManagedThread::getTarget)
                .filter(t -> t instanceof PolyhedronFactory)
                .map(t -> (PolyhedronFactory) t)
                .findFirst()
                .ifPresent(factory -> Logger.info(String.valueOf(factory.getThreadedArea())));
    }


}