package org.spring1.realwordjob.controller;

import org.spring1.realwordjob.DTO.UserDTO;
import org.spring1.realwordjob.DTO.UserProfileDTO;
import org.spring1.realwordjob.model.User;
import org.spring1.realwordjob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Get all users (raw)
    @GetMapping("/raw-all")
    public List<User> findAllRaw() {
        return userService.findAll();
    }

    // ✅ Find user by ID
    @GetMapping("/find/{id}")
    public User findUserById(@PathVariable long id) {
        return userService.findUser(id);
    }

    // ✅ Delete user (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    // ✅ Get all users as DTO (with profile fields)
    @GetMapping("/all")
    public List<UserDTO> findAllUsers() {
        return userService.findAll().stream()
                .map(u -> new UserDTO(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole(),
                        u.getBio(),
                        u.getProfilePicUrl(),
                        u.isOnlineStatus()
                ))
                .toList();
    }

    // ✅ Search users by keyword (Teams-like)
    @GetMapping("/search")
    public List<UserProfileDTO> searchUsers(@RequestParam String keyword) {
        return userService.searchUsers(keyword);
    }

    // ✅ Get user profile by ID
    @GetMapping("/profile/{id}")
    public UserProfileDTO getUserProfile(@PathVariable Long id) {
        return userService.getUserProfile(id);
    }

    // ✅ Update user profile (bio, profilePic, online status)
    @PutMapping("/update/{id}")
    public UserDTO updateUserProfile(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.updateUserProfile(
                id,
                userDTO.getBio(),
                userDTO.getProfilePicUrl(),
                userDTO.isOnlineStatus()
        );
    }
}
