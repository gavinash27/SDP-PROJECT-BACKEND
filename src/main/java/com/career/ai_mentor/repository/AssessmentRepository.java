package com.career.ai_mentor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.career.ai_mentor.model.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {
}