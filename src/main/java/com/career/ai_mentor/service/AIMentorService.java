package com.career.ai_mentor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.career.ai_mentor.model.Response;
import com.career.ai_mentor.model.Result;
import com.career.ai_mentor.model.Roadmap;
import com.career.ai_mentor.model.ChatMessage;
import com.career.ai_mentor.model.Student;

import com.career.ai_mentor.repository.ResultRepository;
import com.career.ai_mentor.repository.RoadmapRepository;
import com.career.ai_mentor.repository.StudentRepository;
import com.career.ai_mentor.repository.ChatRepository;

@Service
public class AIMentorService {

    @Autowired
    private ResultRepository resultRepo;

    @Autowired
    private RoadmapRepository roadmapRepo;

    @Autowired
    private AIClientService aiClientService;

    @Autowired
    private ChatRepository chatRepo;

    @Autowired
    private StudentRepository studentRepo;

    // 🔥 FIXED ANALYZE METHOD
    public Map<String, Object> analyze(List<Response> responses, String education) {

        int analytical = 0;
        int biological = 0;
        int creative = 0;

        for (Response r : responses) {

            int score = 0;

            switch (r.getAnswer().toLowerCase()) {
                case "low": score = 20; break;
                case "medium": score = 50; break;
                case "high": score = 80; break;
            }

            if (r.getQuestionId() % 3 == 1) analytical += score;
            else if (r.getQuestionId() % 3 == 2) biological += score;
            else creative += score;
        }

        // ✅ STEP-5.4 FIX (VERY IMPORTANT)
        int studentId = responses.get(0).getStudentId();

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

         // ✅ NOW FIXED

        String career = "";

        if (education.equalsIgnoreCase("10th")) {
            if (analytical > biological && analytical > creative)
                career = "MPC (Engineering)";
            else if (biological > analytical)
                career = "BiPC (Medical)";
            else
                career = "Commerce / Creative";
        }

        else if (education.equalsIgnoreCase("Intermediate")) {
            if (analytical > biological)
                career = "Software Engineering / AI / Data Science";
            else
                career = "MBBS / Medical Field";
        }

        else if (education.equalsIgnoreCase("B.Tech")) {
            if (analytical > creative)
                career = "Software Developer";
            else
                career = "Frontend / UI-UX Developer";
        }

        else if (education.equalsIgnoreCase("MBBS")) {
            if (biological > analytical)
                career = "MD / Clinical Practice";
            else
                career = "Medical Research / Public Health";
        }

        // ✅ SAVE RESULT
        Result res = new Result();
        res.setStudentId(studentId);
        res.setAnalytical(analytical);
        res.setBiological(biological);
        res.setCreative(creative);
        res.setCareer(career);

        resultRepo.save(res);

        String roadmapText = generateRoadmap(career);

        Roadmap roadmap = new Roadmap();
        roadmap.setStudentId(studentId);
        roadmap.setCareer(career);
        roadmap.setSteps(roadmapText);

        roadmapRepo.save(roadmap);

        Map<String, Object> result = new HashMap<>();
        result.put("analytical", analytical);
        result.put("biological", biological);
        result.put("creative", creative);
        result.put("career", career);
        result.put("roadmap", roadmapText);

        return result;
    }

    public String generateRoadmap(String career) {

        if (career.contains("Software")) {
            return "1. Learn Java/Python\n"
                 + "2. Master DSA\n"
                 + "3. Build Projects\n"
                 + "4. Learn React & Spring Boot\n"
                 + "5. Prepare for interviews";
        } 
        else if (career.contains("Medical")) {
            return "1. Prepare for NEET/PG\n"
                 + "2. Strengthen Biology\n"
                 + "3. Clinical Practice\n"
                 + "4. Specialization";
        } 
        else if (career.contains("MPC")) {
            return "1. Choose MPC\n2. Prepare for JEE/EAMCET\n3. Join Engineering";
        } 
        else if (career.contains("BiPC")) {
            return "1. Choose BiPC\n2. Prepare for NEET\n3. Join MBBS";
        } 
        else {
            return "1. Explore skills\n2. Build portfolio\n3. Internships\n4. Jobs";
        }
    }

    public String chat(int studentId, String message) {

        // 🔥 1. Get Student
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 🔥 2. Get Latest Result
        List<Result> results = resultRepo.findByStudentId(studentId);
        Result latest = results.isEmpty() ? null : results.get(results.size() - 1);

        // 🔥 3. Get Roadmap
        List<Roadmap> roadmaps = roadmapRepo.findByStudentId(studentId);
        String roadmapText = roadmaps.isEmpty() ? "No roadmap yet" : roadmaps.get(0).getSteps();

        // 🔥 4. Build AI Context
        String context = buildContext(student, latest, roadmapText);

        // 🔥 5. Final Prompt
        String finalPrompt = context + "\n\nStudent Question: " + message;

        // 🔥 6. Call AI
        String reply = aiClientService.getAIResponse(finalPrompt);

        // 🔥 Save Chat
        ChatMessage chat = new ChatMessage();
        chat.setStudentId(studentId);
        chat.setMessage(message);
        chat.setResponse(reply);

        chatRepo.save(chat);

        return reply;
    }
    
    private String buildContext(Student student, Result result, String roadmap) {

        StringBuilder sb = new StringBuilder();

        sb.append("You are an AI Career Mentor.\n");

        sb.append("Student Profile:\n");
        sb.append("Name: ").append(student.getName()).append("\n");
        sb.append("Education: ").append(student.getEducationLevel()).append("\n\n");

        if (result != null) {
            sb.append("Assessment Result:\n");
            sb.append("Analytical: ").append(result.getAnalytical()).append("\n");
            sb.append("Biological: ").append(result.getBiological()).append("\n");
            sb.append("Creative: ").append(result.getCreative()).append("\n");
            sb.append("Suggested Career: ").append(result.getCareer()).append("\n\n");
        }

        sb.append("Roadmap:\n").append(roadmap).append("\n\n");

        sb.append("Instructions:\n");
        sb.append("- Give personalized career advice\n");
        sb.append("- Be simple and clear\n");
        sb.append("- Guide step-by-step\n");

        return sb.toString();
    }
}