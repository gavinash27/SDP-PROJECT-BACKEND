package com.career.ai_mentor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.career.ai_mentor.model.Student;
import com.career.ai_mentor.model.Roadmap;
import com.career.ai_mentor.security.JwtUtil;
import com.career.ai_mentor.service.StudentService;
import com.career.ai_mentor.repository.RoadmapRepository;

import java.util.*;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoadmapRepository roadmapRepo; // 🔥 NEW

    // ✅ Register
    @PostMapping("/register")
    public Student register(@RequestBody Student student) {
        return studentService.register(student);
    }

    // ✅ Login
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Student student) {

        Student s = studentService.login(student.getEmail(), student.getPassword());

        if (s != null) {
            String token = jwtUtil.generateToken(s.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("studentId", s.getId());

            return response;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    // 🔥 UPDATED: Dashboard API (WITH ROADMAP)
    @GetMapping("/dashboard/{id}")
    public Map<String, Object> getDashboard(@PathVariable int id) {

        Map<String, Object> response = studentService.getDashboardData(id);

        // 🔥 ADD ROADMAP
        List<Roadmap> roadmapList = roadmapRepo.findByStudentId(id);

        if (roadmapList != null && !roadmapList.isEmpty()) {
            response.put("roadmap", roadmapList.get(roadmapList.size() - 1).getSteps());
        } else {
            response.put("roadmap", null);
        }

        return response;
    }
}