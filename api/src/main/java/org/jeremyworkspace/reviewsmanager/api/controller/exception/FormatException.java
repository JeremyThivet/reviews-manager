package org.jeremyworkspace.reviewsmanager.api.controller.exception;

import net.bytebuddy.implementation.bytecode.Throw;

public class FormatException extends Exception{

    public FormatException(String message, Throwable err){
        super(message, err);
    }

    public FormatException(String message){
        super(message);
    }

}
