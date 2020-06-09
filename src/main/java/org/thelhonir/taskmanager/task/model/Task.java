package org.thelhonir.taskmanager.task.model;

import java.util.Objects;
import java.util.UUID;

import org.thelhonir.taskmanager.status.Status;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
public class Task {
    
    private UUID id;
    @NonNull
    private String description;
    @Builder.Default
    private Status status = Status.TODO;

    public Task(UUID id, String description, Status status) {
        this.id = id;
        this.description = description;
        this.status = Objects.isNull(status) ? Status.TODO : status;
    }
}