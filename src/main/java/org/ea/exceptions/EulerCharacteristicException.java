package org.ea.exceptions;

public class EulerCharacteristicException extends GeometryException {
    public EulerCharacteristicException() {
        super(ExceptionMessages.INVALID_EULER_CHARACTERISTIC);
    }
}
