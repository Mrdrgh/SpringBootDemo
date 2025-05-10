package com.example.mrdrgh.demo.service;

import com.example.mrdrgh.demo.dto.TaskStatusUpdateDto;
import com.example.mrdrgh.demo.model.Task;
import com.example.mrdrgh.demo.model.User;
import com.example.mrdrgh.demo.repository.TaskRepository;
import com.example.mrdrgh.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User assignedUser;
    private User otherUser;
    private Task task;

    @BeforeEach
    void setUp() {
        assignedUser = new User();
        assignedUser.setId(1L);
        assignedUser.setName("Assigned User");
        assignedUser.setEmail("assigned@example.com");
        assignedUser.setPassword("password");
        assignedUser.setRole(User.Role.USER);

        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setName("Other User");
        otherUser.setEmail("other@example.com");
        otherUser.setPassword("password");
        otherUser.setRole(User.Role.USER);

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(Task.Status.À_FAIRE);
        task.setAssignedUser(assignedUser);
    }

    @Test
    void updateTaskStatus_WhenTaskNotFound_ShouldThrowEntityNotFoundException() {
        // Arrange
        TaskStatusUpdateDto statusDto = new TaskStatusUpdateDto();
        statusDto.setStatus(Task.Status.EN_COURS);
        
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            taskService.updateTaskStatus(1L, statusDto, "assigned@example.com")
        );
        
        verify(taskRepository).findById(1L);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void updateTaskStatus_WhenUserNotFound_ShouldThrowUsernameNotFoundException() {
        // Arrange
        TaskStatusUpdateDto statusDto = new TaskStatusUpdateDto();
        statusDto.setStatus(Task.Status.EN_COURS);
        
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("assigned@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> 
            taskService.updateTaskStatus(1L, statusDto, "assigned@example.com")
        );
        
        verify(taskRepository).findById(1L);
        verify(userRepository).findByEmail("assigned@example.com");
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void updateTaskStatus_WhenUserNotAssigned_ShouldThrowAccessDeniedException() {
        // Arrange
        TaskStatusUpdateDto statusDto = new TaskStatusUpdateDto();
        statusDto.setStatus(Task.Status.EN_COURS);
        
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(otherUser));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> 
            taskService.updateTaskStatus(1L, statusDto, "other@example.com")
        );
        
        verify(taskRepository).findById(1L);
        verify(userRepository).findByEmail("other@example.com");
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void updateTaskStatus_WhenUserIsAssigned_ShouldUpdateStatus() {
        // Arrange
        TaskStatusUpdateDto statusDto = new TaskStatusUpdateDto();
        statusDto.setStatus(Task.Status.EN_COURS);
        
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("assigned@example.com")).thenReturn(Optional.of(assignedUser));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = taskService.updateTaskStatus(1L, statusDto, "assigned@example.com");

        // Assert
        assertEquals(Task.Status.EN_COURS, result.getStatus());
        verify(taskRepository).findById(1L);
        verify(userRepository).findByEmail("assigned@example.com");
        verify(taskRepository).save(task);
    }
}
