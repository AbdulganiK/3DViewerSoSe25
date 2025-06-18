package org.ea.exceptions;

public class NotAClosedPolygonException extends GeometryException{
    public NotAClosedPolygonException() {
        super(ExceptionMessages.POLYGON_IS_NOT_CLOSED);
    }
}
