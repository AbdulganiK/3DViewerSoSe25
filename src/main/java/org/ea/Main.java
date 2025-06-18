package org.ea;

import org.ea.constant.Arguments;
import org.ea.controller.PolyhedronController;
import org.ea.exceptions.STLReaderException;
import org.ea.model.Triangle;
import org.ea.utiltities.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
                                            .buildTriangles(STLFileReaderSelector
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
        LinkedBlockingQueue<List<Float>> triangleDataQueue = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Triangle> triangleQueue = new LinkedBlockingQueue<>();
        try {
            TriangleFactory triangleFactory = new TriangleFactory(triangleDataQueue, triangleQueue);
            PolyhedronFactory polyhedronFactory = new PolyhedronFactory(triangleQueue);
            Thread readerThread = new Thread(new STLByteReader(new File(fileName), triangleDataQueue));
            Thread triangleFactoryThread = new Thread(triangleFactory);
            Thread polyhedronFactoryThread = new Thread(polyhedronFactory);
            readerThread.start();
            triangleFactoryThread.start();
            polyhedronFactoryThread.start();
            readerThread.join();
            triangleFactoryThread.join();
            polyhedronFactoryThread.join();
            System.out.println(polyhedronFactory.getArea());
        } catch (STLReaderException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(Arguments.EXIT_ERROR);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}