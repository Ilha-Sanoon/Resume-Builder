package com.foliofield.resume.service;

import com.foliofield.resume.dto.ResumeRequest;
import com.foliofield.resume.model.Resume;
import com.foliofield.resume.repository.ResumeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ResumeService {
    private final AtomicLong nextId = new AtomicLong(4);
    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
        seedDemoData();
    }

    public List<Resume> findAll() {
        return resumeRepository.findAllByOrderByIdAsc();
    }

    public List<Resume> findAllByOwnerEmail(String ownerEmail) {
        return resumeRepository.findAllByOwnerEmailIgnoreCaseOrderByIdDesc(ownerEmail.trim().toLowerCase());
    }

    public Resume findById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));
    }

    public Resume findByIdAndOwnerEmail(Long id, String ownerEmail) {
        return resumeRepository.findByIdAndOwnerEmailIgnoreCase(id, ownerEmail.trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));
    }

    public Resume create(ResumeRequest request) {
        return save(request);
    }

    public Resume create(ResumeRequest request, String ownerEmail) {
        Resume resume = save(request);
        resume.setOwnerEmail(ownerEmail.trim().toLowerCase());
        return resumeRepository.save(resume);
    }

    public Resume update(Long id, ResumeRequest request) {
        findById(id);
        Resume updated = toResume(id, request);
        return resumeRepository.save(updated);
    }

    public Resume update(Long id, ResumeRequest request, String ownerEmail) {
        Resume existing = findById(id);
        if (existing.getOwnerEmail() == null || !existing.getOwnerEmail().equalsIgnoreCase(ownerEmail.trim())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found");
        }
        Resume updated = toResume(id, request);
        updated.setOwnerEmail(ownerEmail.trim().toLowerCase());
        return resumeRepository.save(updated);
    }

    public void delete(Long id) {
        findById(id);
        resumeRepository.deleteById(id);
    }

    private Resume save(ResumeRequest request) {
        Long id = nextId.getAndIncrement();
        Resume resume = toResume(id, request);
        return resumeRepository.save(resume);
    }

    private void seedDemoData() {
        if (resumeRepository.count() == 0) {
            save(new ResumeRequest("Product Designer Resume", "Product Designer", "New York, NY", "jordan@example.com", "Product designer with 6+ years creating thoughtful digital experiences.", "Led product design projects from discovery to launch.", "BFA, Interaction Design", "Figma, Prototyping, Design systems", "Modern", "Confident", "Ocean"));
            save(new ResumeRequest("Senior Product Manager", "Product Manager", "Austin, TX", "jordan@example.com", "Product leader focused on clear strategy and measurable outcomes.", "Owned product strategy and cross-functional delivery.", "MBA, Business Administration", "Roadmapping, Strategy, Analytics", "Professional", "Classic", "Forest"));
            save(new ResumeRequest("Creative Portfolio Resume", "Creative Director", "Los Angeles, CA", "jordan@example.com", "Creative leader building memorable brands and thoughtful experiences.", "Built memorable brands and creative campaigns.", "BA, Visual Communication", "Brand strategy, Art direction, Storytelling", "Creative", "Creative", "Terracotta"));
        }
    }

    private Resume toResume(Long id, ResumeRequest request) {
        return new Resume(id, request.name(), request.targetRole(), request.location(), request.email(),
                request.summary(), request.experience(), request.education(), request.skills(),
                request.template(), request.tone(), request.accent());
    }
}
