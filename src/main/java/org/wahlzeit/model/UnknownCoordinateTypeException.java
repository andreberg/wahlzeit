package org.wahlzeit.model;

public class UnknownCoordinateTypeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnknownCoordinateTypeException(String reason)	{
        super(reason);
    }
}
