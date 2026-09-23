package com.foliofield.resume.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResumeRequest(
        @NotBlank String name,
        @NotBlank String targetRole,
        String location,
        @Email String email,
        String summary,
        String experience,
        String education,
        String skills,
        @NotBlank String template,
        String tone,
        String accent
) {
}
