package com.career.ai_mentor.model;

import jakarta.persistence.*;

@Entity
@Table(name = "result")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "student_id") // 🔥 mapping
    private int studentId;

    private int analytical;
    private int biological;
    private int creative;
    private String education;

    private String career;

    // Getters & Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getAnalytical() { return analytical; }
    public void setAnalytical(int analytical) { this.analytical = analytical; }

    public int getBiological() { return biological; }
    public void setBiological(int biological) { this.biological = biological; }

    public int getCreative() { return creative; }
    public void setCreative(int creative) { this.creative = creative; }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
    
    public String getCareer() { return career; }
    public void setCareer(String career) { this.career = career; }
}