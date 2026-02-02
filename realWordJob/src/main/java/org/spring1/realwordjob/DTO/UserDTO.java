package org.spring1.realwordjob.DTO;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String bio;
    private String profilePicUrl;
    private boolean onlineStatus;

    // Full constructor
    public UserDTO(Long id, String name, String email, String role, String bio, String profilePicUrl, boolean onlineStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.bio = bio;
        this.profilePicUrl = profilePicUrl;
        this.onlineStatus = onlineStatus;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getBio() { return bio; }
    public String getProfilePicUrl() { return profilePicUrl; }
    public boolean isOnlineStatus() { return onlineStatus; }
}
