package org.ea.exceptions;

public class EndOfFileReachedException extends STLReaderException{
    public EndOfFileReachedException(){
        super(ExceptionMessages.END_OF_FILE_REACHED);
    }
}
