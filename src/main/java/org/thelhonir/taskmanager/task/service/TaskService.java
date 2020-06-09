package org.thelhonir.taskmanager.task.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.thelhonir.taskmanager.status.Status;
import org.thelhonir.taskmanager.task.controller.exception.TaskDescriptionNullException;
import org.thelhonir.taskmanager.task.controller.exception.TaskNotFoundException;
import org.thelhonir.taskmanager.task.model.Task;

@Service
public class TaskService {

    public HashMap<UUID, Task> tasksRepo = new HashMap<>();

    public Task newTask(String description, Status status) throws TaskDescriptionNullException, TaskNotFoundException {

        if (Objects.isNull(description)) {
            throw new TaskDescriptionNullException("Description cannot be empty");
        }

        UUID id = UUID.randomUUID();
        tasksRepo.put(id, Task.builder().id(id).description(description).status(status).build());
        return getTask(id);
    }

    public Task updateTask(UUID id, String description, Status status) throws TaskNotFoundException {
        String updatedDescription = Objects.nonNull(description) ? description : getTask(id).getDescription();
        tasksRepo.replace(id, getTask(id), Task.builder()
                                                .id(id)
                                                .description(updatedDescription)
                                                .status(status)
                                                .build());
        return getTask(id);
    }

    public List<Task> getTasks(Optional<String> statusCode) {
        return this.tasksRepo.values().stream().filter(task -> {
            if(statusCode.isPresent()){
                return task.getStatus().toString().equals(statusCode.get());
            } else {
                return true;
            }
        }).collect(Collectors.toList());
    }

    public Task getTask(UUID id) throws TaskNotFoundException {
        Task task = tasksRepo.get(id);
        if (Objects.isNull(task)) {
            throw new TaskNotFoundException("Task not found for ID: " + id);
        }
        return task;
    }

    public void deleteTasks() {
        this.tasksRepo.clear();
    }

    public UUID deleteTask(UUID id) throws TaskNotFoundException {
        getTask(id);
        tasksRepo.remove(id);
        return id;
    }
}