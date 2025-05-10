package com.example.mrdrgh.demo.dto;

import com.example.mrdrgh.demo.model.Task;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusUpdateDto {
    @NotNull(message = "Status is required")
    private Task.Status status;
}
