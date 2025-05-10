package com.example.mrdrgh.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.À_FAIRE;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedUser;

    public enum Status {
        À_FAIRE, EN_COURS, TERMINÉE
    }
}
