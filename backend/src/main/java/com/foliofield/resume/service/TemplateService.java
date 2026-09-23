package com.foliofield.resume.service;

import com.foliofield.resume.model.Template;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {
    private final List<Template> templates = List.of(
            new Template("Simple", "Simple", "Clean essentials with effortless scanning.", "simple"),
            new Template("Modern", "Medium", "Clear hierarchy with a confident accent.", "modern"),
            new Template("Minimal", "Simple", "Quiet typography and generous breathing room.", "minimal"),
            new Template("Balanced", "Medium", "A polished layout for every career stage.", "balanced"),
            new Template("Professional", "Professional", "Classic structure for experienced professionals.", "professional"),
            new Template("Executive", "High impact", "Commanding detail for leadership applications.", "executive"),
            new Template("Creative", "Medium", "Expressive structure for visual thinkers.", "creative"),
            new Template("Compact", "Simple", "One-page focus for fast-moving applications.", "compact"),
            new Template("Premium", "High impact", "Editorial polish with room for your story.", "premium"),
            new Template("Model", "Model portfolio", "Image-led presentation for portfolios and talent.", "model")
    );

    public List<Template> findAll() {
        return templates;
    }
}
