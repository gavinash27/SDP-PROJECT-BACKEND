package com.career.ai_mentor.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.career.ai_mentor.model.Result;

public interface ResultRepository extends JpaRepository<Result, Integer> {

    List<Result> findByStudentId(int studentId); // 🔥 IMPORTANT
}