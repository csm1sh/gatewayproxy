package com.hagongda.lightmodbus.exceptions;

public class MDSlaveException extends Exception
{
    private static final long serialVersionUID = 1L;

    public MDSlaveException()
    {
    }

    public MDSlaveException(String message)
    {
        super(message);
    }
}
