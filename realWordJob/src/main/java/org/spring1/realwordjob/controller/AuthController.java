package org.spring1.realwordjob.controller;

import org.spring1.realwordjob.model.AuthRequest;
import org.spring1.realwordjob.model.AuthResponse;
import org.spring1.realwordjob.model.User;
import org.spring1.realwordjob.security.JwtUtil;
import org.spring1.realwordjob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    // ✅ Signup
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        // Password encoding handled in UserService
        return ResponseEntity.ok(service.addUser(user));
    }

    // ✅ Login with role and email returned
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(), authRequest.getPassword()
                    )
            );

            String token = jwtUtil.generateToken(authRequest.getEmail());

            Optional<User> optionalUser = service.findByEmail(authRequest.getEmail());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(401).body("User not found");
            }

            User user = optionalUser.get();

            // Return token, userId, role, and email
            AuthResponse response = new AuthResponse(token, user.getId(), user.getRole(), user.getEmail());



            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }
}
