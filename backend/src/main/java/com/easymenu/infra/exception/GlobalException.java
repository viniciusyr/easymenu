package com.easymenu.infra.exception;

import lombok.Getter;
import lombok.Setter;
import org.zalando.problem.Status;

@Getter
@Setter
public class GlobalException extends RuntimeException{

    private final String type;
    private final String title;
    private final Status status;

    public GlobalException(String message, String type, String title, Status status){
        super(message);
        this.type = type;
        this.title = title;
        this.status = status;
    }

}
