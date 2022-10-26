package org.jeremyworkspace.reviewsmanager.api.controller;

import net.bytebuddy.implementation.bytecode.Throw;

public class FormatException extends Exception{

    public FormatException(String message, Throwable err){
        super(message, err);
    }

}
