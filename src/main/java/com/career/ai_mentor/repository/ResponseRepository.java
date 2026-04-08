package com.career.ai_mentor.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.career.ai_mentor.model.Response;

public interface ResponseRepository extends JpaRepository<Response, Integer> {

    List<Response> findByStudentId(int studentId); // 🔥 IMPORTANT
}