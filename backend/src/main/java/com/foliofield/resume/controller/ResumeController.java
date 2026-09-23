package com.foliofield.resume.controller;

import com.foliofield.resume.dto.ResumeRequest;
import com.foliofield.resume.model.Resume;
import com.foliofield.resume.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public List<Resume> getResumes() {
        return resumeService.findAll();
    }

    @GetMapping(params = "ownerEmail")
    public List<Resume> getUserResumes(@RequestParam String ownerEmail) {
        return resumeService.findAllByOwnerEmail(ownerEmail);
    }

    @GetMapping("/{id}")
    public Resume getResume(@PathVariable Long id) {
        return resumeService.findById(id);
    }

    @GetMapping(value = "/{id}", params = "ownerEmail")
    public Resume getUserResume(@PathVariable Long id, @RequestParam String ownerEmail) {
        return resumeService.findByIdAndOwnerEmail(id, ownerEmail);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resume createResume(@Valid @RequestBody ResumeRequest request) {
        return resumeService.create(request);
    }

    @PostMapping(params = "ownerEmail")
    @ResponseStatus(HttpStatus.CREATED)
    public Resume createUserResume(@RequestParam String ownerEmail, @Valid @RequestBody ResumeRequest request) {
        return resumeService.create(request, ownerEmail);
    }

    @PutMapping("/{id}")
    public Resume updateResume(@PathVariable Long id, @Valid @RequestBody ResumeRequest request) {
        return resumeService.update(id, request);
    }

    @PutMapping(value = "/{id}", params = "ownerEmail")
    public Resume updateUserResume(@PathVariable Long id, @RequestParam String ownerEmail, @Valid @RequestBody ResumeRequest request) {
        return resumeService.update(id, request, ownerEmail);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResume(@PathVariable Long id) {
        resumeService.delete(id);
    }
}
