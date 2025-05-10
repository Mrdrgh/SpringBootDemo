package com.example.mrdrgh.demo.repository;

import com.example.mrdrgh.demo.model.Task;
import com.example.mrdrgh.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedUser(User user);
}
