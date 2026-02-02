package org.spring1.realwordjob.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String bio;
    private String profilePicUrl;
    private boolean onlineStatus;
}
