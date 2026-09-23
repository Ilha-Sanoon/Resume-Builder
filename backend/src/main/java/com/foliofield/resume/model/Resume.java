package com.foliofield.resume.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "resumes")
public class Resume {
    @Id
    private Long id;
    private String name;
    private String targetRole;
    private String location;
    private String email;
    private String summary;
    private String experience;
    private String education;
    private String skills;
    private String template;
    private String tone;
    private String accent;
    private String ownerEmail;

    public Resume() {
    }

    public Resume(Long id, String name, String targetRole, String location, String email,
                  String summary, String experience, String education, String skills,
                  String template, String tone, String accent) {
        this.id = id;
        this.name = name;
        this.targetRole = targetRole;
        this.location = location;
        this.email = email;
        this.summary = summary;
        this.experience = experience;
        this.education = education;
        this.skills = skills;
        this.template = template;
        this.tone = tone;
        this.accent = accent;
    }

    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public String getOwnerEmail() { return ownerEmail; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getTemplate() { return template; }
    public void setTemplate(String template) { this.template = template; }
    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }
    public String getAccent() { return accent; }
    public void setAccent(String accent) { this.accent = accent; }
}
