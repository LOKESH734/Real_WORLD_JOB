package org.spring1.realwordjob.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;   // ✅ use userId instead of generic "id"
    private String role;
    private String email;
}
