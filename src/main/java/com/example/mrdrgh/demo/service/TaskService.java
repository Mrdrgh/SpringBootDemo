package com.example.mrdrgh.demo.service;

import com.example.mrdrgh.demo.dto.TaskDto;
import com.example.mrdrgh.demo.dto.TaskStatusUpdateDto;
import com.example.mrdrgh.demo.model.Task;
import com.example.mrdrgh.demo.model.User;
import com.example.mrdrgh.demo.repository.TaskRepository;
import com.example.mrdrgh.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskDto createTask(TaskDto taskDto, String currentUserEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        User assignedUser;
        if (taskDto.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(taskDto.getAssignedUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Assigned user not found"));
        } else {
            assignedUser = currentUser; // Assign to current user if not specified
        }

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(Task.Status.À_FAIRE); // Default status
        task.setAssignedUser(assignedUser);

        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    public List<TaskDto> getUserTasks(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return taskRepository.findByAssignedUser(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TaskDto updateTaskStatus(Long taskId, TaskStatusUpdateDto statusDto, String userEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // Check if the current user is the assigned user
        if (!task.getAssignedUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Only the assigned user can update the task status");
        }
        
        task.setStatus(statusDto.getStatus());
        Task updatedTask = taskRepository.save(task);
        
        return mapToDto(updatedTask);
    }

    private TaskDto mapToDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .assignedUserId(task.getAssignedUser().getId())
                .build();
    }
}
