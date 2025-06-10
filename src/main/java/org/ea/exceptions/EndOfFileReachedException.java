package org.ea.exceptions;

public class EndOfFileReachedException extends Exception{
    public EndOfFileReachedException(){
        super(ExceptionMessages.END_OF_FILE_REACHED);
    }
}
