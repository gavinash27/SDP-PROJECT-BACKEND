package com.career.ai_mentor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.career.ai_mentor.model.Roadmap;
import java.util.List;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Integer> {
	List<Roadmap> findByStudentId(int studentId);
}