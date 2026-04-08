package com.career.ai_mentor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.career.ai_mentor.model.Assessment;
import com.career.ai_mentor.model.Response;
import com.career.ai_mentor.model.Result;
import com.career.ai_mentor.model.Roadmap;
import com.career.ai_mentor.repository.AssessmentRepository;
import com.career.ai_mentor.repository.ResultRepository;
import com.career.ai_mentor.repository.RoadmapRepository;
import com.career.ai_mentor.service.AIMentorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/ai")
@CrossOrigin("http://localhost:5173/")
public class AIMentorController {

    @Autowired
    private AIMentorService service;

    @Autowired
    private RoadmapRepository roadmapRepo;
    
    @Autowired
    private ResultRepository resultRepo;
    
    @Autowired
    private AssessmentRepository repo;

    @Autowired
    private ObjectMapper objectMapper;

    // 📊 History
    @GetMapping("/history/{studentId}")
    public List<Result> getHistory(@PathVariable int studentId) {
        return resultRepo.findByStudentId(studentId);
    }

    // ➕ Add Question
    @PostMapping("/admin/add-question")
    public Assessment addQuestion(@RequestBody Assessment a) {
        return repo.save(a);
    }

    // 🛣 Roadmap
    @GetMapping("/roadmap/{studentId}")
    public List<Roadmap> getRoadmap(@PathVariable int studentId) {
        return roadmapRepo.findByStudentId(studentId);
    }

    // 🔥 FIXED ANALYZE (FINAL VERSION)
    @PostMapping("/analyze")
    public Map<String, Object> analyze(@RequestBody Map<String, Object> request) {

        if (request.get("responses") == null || request.get("education") == null) {
            throw new RuntimeException("Invalid request: responses or education missing");
        }

        List<Response> responses = objectMapper.convertValue(
                request.get("responses"),
                objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, Response.class)
        );

        String education = request.get("education").toString();

        return service.analyze(responses, education);
    }

    // 🤖 Chatbot
    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> input) {

        if (input.get("studentId") == null || input.get("message") == null) {
            return "Invalid request";
        }

        int studentId = Integer.parseInt(input.get("studentId"));
        String message = input.get("message");

        return service.chat(studentId, message);
    }
}