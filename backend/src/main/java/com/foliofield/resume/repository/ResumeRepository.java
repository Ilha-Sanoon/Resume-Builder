package com.foliofield.resume.repository;

import com.foliofield.resume.model.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResumeRepository extends MongoRepository<Resume, Long> {
	List<Resume> findAllByOrderByIdAsc();
	List<Resume> findAllByOwnerEmailIgnoreCaseOrderByIdDesc(String ownerEmail);
	java.util.Optional<Resume> findByIdAndOwnerEmailIgnoreCase(Long id, String ownerEmail);
}