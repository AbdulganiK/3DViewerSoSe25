package org.ea.exceptions;

public class OffsetOutOfRangeException extends Exception{
    public OffsetOutOfRangeException() {
        super(ExceptionMessages.OFFSET_OUT_OF_RANGE);
    }
}
