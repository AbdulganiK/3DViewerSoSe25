package org.ea;

import com.sun.jdi.ThreadGroupReference;
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

    public static void main(String[] args){
        doTask3(args[Arguments.FILE_NAME_ARGUMENT]);
    }

    private static Triangle[] doTask2(String fileName) {
        try {
            return new PolyhedronController(
                    new PolyhedronFactory().buildPolyhedron(
                            new TriangleFactory().buildTriangles(
                                    new STLByteReader(
                                            new File(fileName), new LinkedBlockingQueue<List<Float>>())
                                                .readTriangleData())))
                                                    .getSortedSurfaces();
        } catch (STLReaderException | IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
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
            System.exit(-1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}