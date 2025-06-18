package org.ea.exceptions;

public class NotATriangleException extends GeometryException {
    public NotATriangleException() {
        super(ExceptionMessages.NOT_A_TRIANGLE);
    }
}
