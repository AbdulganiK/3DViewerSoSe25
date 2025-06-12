package org.ea.exceptions;

public class NotAStlFileException extends STLReaderException {
    public NotAStlFileException(){
        super(ExceptionMessages.NOT_A_STL_FILE);
    }
}
