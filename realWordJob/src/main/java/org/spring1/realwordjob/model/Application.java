package org.spring1.realwordjob.model;

import jakarta.persistence.*;
import lombok.*;
import org.spring1.realwordjob.model.ApplicationStatus;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long jobId;

    @Lob
    private byte[] resume;

    // ✅ New status field (default = PENDING)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;
}
