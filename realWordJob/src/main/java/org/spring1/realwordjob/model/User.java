package org.spring1.realwordjob.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String role;  // store "USER", "ADMIN" etc.

    private String bio;
    private String profilePicUrl;
    private boolean onlineStatus;
}
