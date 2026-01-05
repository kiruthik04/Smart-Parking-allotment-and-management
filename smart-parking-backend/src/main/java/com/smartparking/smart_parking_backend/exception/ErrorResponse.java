//Standard format of saying the error to user. LocalDatetime is used to know where and in what process the error has happened.
package com.smartparking.smart_parking_backend.exception;

import java.time.LocalDateTime;

public class ErrorResponse
{

    private String message; //human readable request
    private LocalDateTime timestamp; //When error occur, this help for debugging and logging.

    public ErrorResponse(String message)
    {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage()
    {
        return message;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

}