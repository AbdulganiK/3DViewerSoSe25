package org.ea.exceptions;

public interface ExceptionMessages {
    String NOT_A_STL_FILE = "Not a .stl file";
    String END_OF_FILE_REACHED = "End of file reached!";
    String OFFSET_OUT_OF_RANGE = "End of file reached!";
    String EDGES_NOT_CONNECTED = "Edges are not connected to each other";
    String POLYGON_IS_NOT_CLOSED = "Polygon is not closed";
    String NOT_ENOUGH_EDGES = "Polygon does not have enough edges. At least 3!";
    String POLYHEDRON_IS_NOT_CLOSED = "Surfaces of Polyhedron are not closed";
    String NOT_A_TRIANGLE = "The given points do not form a triangle.";
    String INVALID_EULER_CHARACTERISTIC = "Invalid Euler characteristic: the formula V - E + F does not hold.";
}
