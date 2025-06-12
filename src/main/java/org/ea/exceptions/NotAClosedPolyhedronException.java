package org.ea.exceptions;

public class NotAClosedPolyhedronException extends Exception{
    public NotAClosedPolyhedronException() {
        super(ExceptionMessages.POLYHEDRON_IS_NOT_CLOSED);
    }
}
