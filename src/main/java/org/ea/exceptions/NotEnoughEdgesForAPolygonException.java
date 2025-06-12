package org.ea.exceptions;

public class NotEnoughEdgesForAPolygonException extends Exception{
    public NotEnoughEdgesForAPolygonException(){
        super(ExceptionMessages.NOT_ENOUGH_EDGES);
    }
}
