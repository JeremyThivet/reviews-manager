package org.jeremyworkspace.reviewsmanager.api.controller.exception;

public class UpdateEntryException extends Exception{

    public UpdateEntryException(String message, Throwable err){
        super(message, err);
    }

    public UpdateEntryException(String message){
        super(message);
    }
}
