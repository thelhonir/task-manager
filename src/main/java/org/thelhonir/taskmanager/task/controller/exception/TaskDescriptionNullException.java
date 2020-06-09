package org.thelhonir.taskmanager.task.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TaskDescriptionNullException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TaskDescriptionNullException(String message) {
        super(message);
    }

}