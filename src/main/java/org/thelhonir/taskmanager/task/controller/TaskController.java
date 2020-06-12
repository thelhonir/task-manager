package org.thelhonir.taskmanager.task.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thelhonir.taskmanager.task.controller.dto.TaskDTO;
import org.thelhonir.taskmanager.task.controller.exception.TaskDescriptionNullException;
import org.thelhonir.taskmanager.task.controller.exception.TaskNotFoundException;
import org.thelhonir.taskmanager.task.model.Task;
import org.thelhonir.taskmanager.task.service.TaskService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
public class TaskController {

    public final static String ENDPOINT_SINGLE = "/task";

    public final static String ENDPOINT = "/tasks";

    private TaskService taskService;

    @PostMapping(path = ENDPOINT_SINGLE, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Task> newTask(@RequestBody TaskDTO task)
            throws TaskDescriptionNullException, TaskNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.taskService.newTask(task.getDescription(), task.getStatus()));
    }

    @PutMapping(path = ENDPOINT_SINGLE + "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody TaskDTO task)
            throws TaskNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.taskService.updateTask(UUID.fromString(id), task.getDescription(), task.getStatus()));
    }

    @GetMapping(path = { ENDPOINT, ENDPOINT + "/{statusCode}" }, produces = "application/json")
    public ResponseEntity<List<Task>> getTasks(@PathVariable(required = false) Optional<String> statusCode) {
        return ResponseEntity.status(HttpStatus.OK).body(this.taskService.getTasks(statusCode));
    }

    @GetMapping(path = ENDPOINT_SINGLE + "/{id}", produces = "application/json")
    public ResponseEntity<Task> getTask(@PathVariable String id) throws TaskNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.taskService.getTask(UUID.fromString(id)));
    }

    @DeleteMapping(path = ENDPOINT)
    public ResponseEntity<String> deleteTasks() {
        this.taskService.deleteTasks();
        return ResponseEntity.status(HttpStatus.OK).body("List deleted");
    }

    @DeleteMapping(path = ENDPOINT_SINGLE + "/{id}")
    public ResponseEntity<UUID> deleteTask(@PathVariable String id) throws TaskNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.taskService.deleteTask(UUID.fromString(id)));
    }

}