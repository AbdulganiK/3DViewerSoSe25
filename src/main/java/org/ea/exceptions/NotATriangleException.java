package org.ea.exceptions;

public class NotATriangleException extends Throwable {
    public NotATriangleException() {
        super(ExceptionMessages.NOT_A_TRIANGLE);
    }
}
