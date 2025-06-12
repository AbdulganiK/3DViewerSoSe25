package org.ea.exceptions;

public class EulerCharacteristicException extends Exception {
    public EulerCharacteristicException() {
        super(ExceptionMessages.INVALID_EULER_CHARACTERISTIC);
    }
}
