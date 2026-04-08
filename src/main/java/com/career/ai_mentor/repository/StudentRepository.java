package com.career.ai_mentor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.career.ai_mentor.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByEmailAndPassword(String email, String password);
}