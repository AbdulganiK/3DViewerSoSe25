package org.ea.exceptions;

public class EdgesAreNotConnectedException extends Exception{
    public EdgesAreNotConnectedException() {
        super(ExceptionMessages.EDGES_NOT_CONNECTED);
    }
}
