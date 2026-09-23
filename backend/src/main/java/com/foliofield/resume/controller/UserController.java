package com.foliofield.resume.controller;

import com.foliofield.resume.dto.SignupRequest;
import com.foliofield.resume.dto.LoginRequest;
import com.foliofield.resume.model.User;
import com.foliofield.resume.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> signup(@Valid @RequestBody SignupRequest request) {
        User user = userService.signup(request);
        return Map.of("id", user.getId(), "name", user.getName(), "email", user.getEmail());
    }

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request);
        return Map.of("id", user.getId(), "name", user.getName(), "email", user.getEmail());
    }
}