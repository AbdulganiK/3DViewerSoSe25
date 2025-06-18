package org.ea.exceptions;

public class NotEnoughEdgesForAPolygonException extends GeometryException{
    public NotEnoughEdgesForAPolygonException(){
        super(ExceptionMessages.NOT_ENOUGH_EDGES);
    }
}
