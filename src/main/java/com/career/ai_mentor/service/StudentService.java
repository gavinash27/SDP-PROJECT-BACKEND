package com.career.ai_mentor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.career.ai_mentor.model.Student;
import com.career.ai_mentor.repository.StudentRepository;
import com.career.ai_mentor.repository.ResponseRepository;
import com.career.ai_mentor.repository.ResultRepository;
import com.career.ai_mentor.repository.AssessmentRepository;
import com.career.ai_mentor.model.Response;
import com.career.ai_mentor.model.Result;
import com.career.ai_mentor.model.Assessment;

import java.util.*;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repo;

    @Autowired
    private ResponseRepository responseRepo;

    @Autowired
    private ResultRepository resultRepo;

    @Autowired
    private AssessmentRepository assessmentRepo;

    public Student register(Student student) {
        student.setRole("STUDENT");
        return repo.save(student);
    }

    public Student login(String email, String password) {
        return repo.findByEmailAndPassword(email, password);
    }

    public Student getStudentById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // 🔥 NEW: Dashboard Data (CLEAN FORMAT)
    public Map<String, Object> getDashboardData(int studentId) {

        Student s = getStudentById(studentId);

        Map<String, Object> response = new HashMap<>();

        response.put("name", s.getName());
        response.put("email", s.getEmail());
        response.put("education", s.getEducationLevel());

        // 🔥 Responses → Clean format
        List<Response> responses = responseRepo.findByStudentId(studentId);
        List<Map<String, String>> assessmentList = new ArrayList<>();

        for (Response r : responses) {
            Assessment q = assessmentRepo.findById(r.getQuestionId()).orElse(null);

            if (q != null) {
                Map<String, String> item = new HashMap<>();
                item.put("question", q.getQuestion());
                item.put("answer", r.getAnswer());
                assessmentList.add(item);
            }
        }

        response.put("assessments", assessmentList);

        // 🔥 Results → Career data
        List<Result> results = resultRepo.findByStudentId(studentId);
        List<Map<String, Object>> careerList = new ArrayList<>();

        for (Result r : results) {
            Map<String, Object> item = new HashMap<>();
            item.put("career", r.getCareer());
            item.put("analytical", r.getAnalytical());
            item.put("biological", r.getBiological());
            item.put("creative", r.getCreative());
            careerList.add(item);
        }

        response.put("careers", careerList);
        response.put("review", "AI-based personalized insights");

        return response;
    }
}