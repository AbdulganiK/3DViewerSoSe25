package org.ea.exceptions;

public class NotAStlFileException extends Exception {
    public NotAStlFileException(){
        super(ExceptionMessages.NOT_A_STL_FILE);
    }
}
