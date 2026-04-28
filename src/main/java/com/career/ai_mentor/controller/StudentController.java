package com.career.ai_mentor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.career.ai_mentor.model.Student;
import com.career.ai_mentor.model.Roadmap;
import com.career.ai_mentor.security.JwtUtil;
import com.career.ai_mentor.service.StudentService;
import com.career.ai_mentor.repository.RoadmapRepository;
import com.career.ai_mentor.repository.StudentRepository;

import java.util.*;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoadmapRepository roadmapRepo;

    @Autowired
    private StudentRepository studentRepo;

    @PostMapping("/register")
    public Student register(@RequestBody Student student) {
        return studentService.register(student);
    }

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

    @GetMapping("/dashboard/{id}")
    public Map<String, Object> getDashboard(@PathVariable int id) {

        Map<String, Object> response = studentService.getDashboardData(id);

        List<Roadmap> roadmapList = roadmapRepo.findByStudentId(id);

        if (roadmapList != null && !roadmapList.isEmpty()) {
            response.put("roadmap", roadmapList.get(roadmapList.size() - 1).getSteps());
        } else {
            response.put("roadmap", null);
        }

        return response;
    }

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @PutMapping("/update/{id}")
    public Student updateStudent(@PathVariable int id, @RequestBody Student updatedStudent) {

        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());
        student.setPassword(updatedStudent.getPassword());
        student.setEducationLevel(updatedStudent.getEducationLevel());

        return studentRepo.save(student);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {

        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        studentRepo.delete(student);
        return "Student deleted successfully";
    }
}