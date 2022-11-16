package org.jeremyworkspace.reviewsmanager.api.controller.exception;

public class WrongOwnerException extends Exception{

    public WrongOwnerException(String message, Throwable err){
        super(message, err);
    }

    public WrongOwnerException(String message){
        super(message);
    }
}
