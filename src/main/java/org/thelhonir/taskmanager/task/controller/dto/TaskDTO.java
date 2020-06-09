package org.thelhonir.taskmanager.task.controller.dto;

import org.thelhonir.taskmanager.status.Status;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDTO {
    private String description;
    private Status status;
}