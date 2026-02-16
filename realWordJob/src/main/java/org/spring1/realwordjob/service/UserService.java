package org.spring1.realwordjob.service;

import org.spring1.realwordjob.DTO.UserDTO;
import org.spring1.realwordjob.DTO.UserProfileDTO;
import org.spring1.realwordjob.model.User;
import org.spring1.realwordjob.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo repo2;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Register user and send welcome email
    public User addUser(User u) {
        if (u.getPassword() != null) {
            // Encode password only if it's not encoded yet
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        }

        User savedUser = repo2.save(u);

        // Welcome email
        String subject = "Welcome to RealWorldJob Portal!";
        String body = "Hi " + savedUser.getName() + ",\n\n" +
                "Your account has been successfully created.\n" +
                "You can now log in and apply for jobs.\n\n" +
                "Best Regards,\nRealWorldJob Team";

        emailService.sendEmail(savedUser.getEmail(), subject, body);

        return savedUser;
    }

    // ✅ Fetch all users
    public List<User> findAll() {
        return repo2.findAll();
    }

    // ✅ Fetch user by ID
    public User findUser(Long id) {
        return repo2.findById(id).orElse(null);
    }

    // ✅ Delete user
    public void delete(Long id) {
        repo2.deleteById(id);
    }

    // ✅ Find user by email
    public Optional<User> findByEmail(String email) {
        return repo2.findByEmail(email);
    }

    // ✅ Search users (Teams-like)
    public List<UserProfileDTO> searchUsers(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return repo2.findAll().stream()
                .filter(u -> {
                    String name = u.getName() != null ? u.getName().toLowerCase() : "";
                    String email = u.getEmail() != null ? u.getEmail().toLowerCase() : "";
                    return name.contains(lowerKeyword) || email.contains(lowerKeyword);
                })
                .map(u -> new UserProfileDTO(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole(),
                        u.getBio(),
                        u.getProfilePicUrl(),
                        u.isOnlineStatus()
                ))
                .collect(Collectors.toList());
    }

    // ✅ Get user profile by ID
    public UserProfileDTO getUserProfile(Long id) {
        return repo2.findById(id)
                .map(u -> new UserProfileDTO(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole(),
                        u.getBio(),
                        u.getProfilePicUrl(),
                        u.isOnlineStatus()
                ))
                .orElse(null);
    }

    // ✅ Update user profile (bio, profilePicUrl, onlineStatus)
    public UserDTO updateUserProfile(Long id, String bio, String profilePicUrl, boolean onlineStatus) {
        User u = repo2.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        u.setBio(bio);
        u.setProfilePicUrl(profilePicUrl);
        u.setOnlineStatus(onlineStatus);

        repo2.save(u);

        return new UserDTO(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getRole(),
                u.getBio(),
                u.getProfilePicUrl(),
                u.isOnlineStatus()
        );
    }

    // ✅ Validate password (for login) – optional helper
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

}
