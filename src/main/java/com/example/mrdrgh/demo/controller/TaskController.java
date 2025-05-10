package com.example.mrdrgh.demo.controller;

import com.example.mrdrgh.demo.dto.TaskDto;
import com.example.mrdrgh.demo.dto.TaskStatusUpdateDto;
import com.example.mrdrgh.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management API")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Create a new task", description = "Create a new task with current user as creator")
    public ResponseEntity<TaskDto> createTask(
            @Valid @RequestBody TaskDto taskDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        TaskDto createdTask = taskService.createTask(taskDto, userDetails.getUsername());
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/my-tasks")
    @Operation(summary = "Get user's tasks", description = "Retrieve all tasks assigned to the current user")
    public ResponseEntity<List<TaskDto>> getUserTasks(@AuthenticationPrincipal UserDetails userDetails) {
        List<TaskDto> tasks = taskService.getUserTasks(userDetails.getUsername());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all tasks", description = "Retrieve all tasks (admin only)")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{taskId}/status")
    @Operation(summary = "Update task status", description = "Update the status of a task (only by assigned user)")
    public ResponseEntity<TaskDto> updateTaskStatus(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskStatusUpdateDto statusDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        TaskDto updatedTask = taskService.updateTaskStatus(taskId, statusDto, userDetails.getUsername());
        return ResponseEntity.ok(updatedTask);
    }
}
