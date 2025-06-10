package org.ea;

import org.ea.constant.FilePaths;
import org.ea.controller.PolyhedronController;
import org.ea.exceptions.EndOfFileReachedException;
import org.ea.exceptions.NotAStlFileException;
import org.ea.exceptions.OffsetOutOfRangeException;
import org.ea.model.Polygon;
import org.ea.model.Polyhedron;
import org.ea.model.Triangle;
import org.ea.utiltities.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args) throws IOException {
        doTask2();
    }

    private static void doTask2() {
        PolyhedronController polyhedronController = null;
        try {
            polyhedronController = new PolyhedronController(
                    PolyhedronFactory.buildPolyhedron(
                            TriangleFactory.buildTriangles(
                                    new STLByteReader(
                                            new File(FilePaths.LARGE_STL_BYTE_FILE)).readTriangleData())));
        } catch (EndOfFileReachedException | NotAStlFileException | OffsetOutOfRangeException | IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        System.out.println(polyhedronController.getSortedSurfaces().length);


    }

}
