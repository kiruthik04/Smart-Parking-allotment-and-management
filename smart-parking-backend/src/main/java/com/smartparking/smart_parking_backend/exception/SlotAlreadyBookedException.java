package com.smartparking.smart_parking_backend.exception;

public class SlotAlreadyBookedException extends RuntimeException
{
    public SlotAlreadyBookedException(String message)
    {
        super(message);
    }
}