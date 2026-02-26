package org.spring1.realwordjob.controller;

import org.spring1.realwordjob.model.*;
import org.spring1.realwordjob.security.JwtUtil;
import org.spring1.realwordjob.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService service;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        return ResponseEntity.ok(service.addUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            User user = service.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 🔥 FINAL FIX
            String token = jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole()
            );

            return ResponseEntity.ok(
                    new AuthResponse(token, user.getId(), user.getRole(), user.getEmail())
            );

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }
}