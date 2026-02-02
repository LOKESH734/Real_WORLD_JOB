package org.spring1.realwordjob.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring1.realwordjob.DTO.JobStatsDTO;
import org.spring1.realwordjob.model.Application;
import org.spring1.realwordjob.model.ApplicationStatus;
import org.spring1.realwordjob.model.User;
import org.spring1.realwordjob.service.ApplicationService;
import org.spring1.realwordjob.service.EmailService;
import org.spring1.realwordjob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private static final Logger log =
            LoggerFactory.getLogger(ApplicationController.class);

    private final ApplicationService applicationService;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public ApplicationController(
            ApplicationService applicationService,
            UserService userService,
            EmailService emailService
    ) {
        this.applicationService = applicationService;
        this.userService = userService;
        this.emailService = emailService;
    }

    // ================= APPLY FOR JOB =================
    @PostMapping(value = "/apply", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> applyForJob(
            @RequestParam Long jobId,
            @RequestPart("resume") MultipartFile resumeFile,
            Authentication authentication
    ) {

        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (resumeFile == null || resumeFile.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Resume file missing"));
        }

        if (applicationService.existsByUserIdAndJobId(user.getId(), jobId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Already applied for this job"));
        }

        try {
            Application application = new Application();
            application.setUserId(user.getId());
            application.setJobId(jobId);
            application.setResume(resumeFile.getBytes());
            application.setStatus(ApplicationStatus.PENDING);

            applicationService.save(application); // ✅ FAST DB SAVE

            // ✅ ASYNC EMAIL (NON-BLOCKING)
            emailService.sendEmail(
                    user.getEmail(),
                    "Application Submitted Successfully",
                    "Hello " + user.getName() +
                            ",\n\nYour application for Job ID " + jobId + " was submitted successfully."
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", "success",
                            "applicationId", application.getId()
                    ));

        } catch (Exception e) {
            log.error("Apply job failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to submit application"));
        }
    }

    // ================= GET ALL APPLICATIONS (ADMIN) =================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.findAll());
    }

    // ================= DOWNLOAD RESUME (ADMIN) =================
    @GetMapping("/{applicationId}/resume")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadResume(@PathVariable Long applicationId) {

        return applicationService.findById(applicationId)
                .map(application -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=resume_" + applicationId + ".docx"
                    );
                    headers.set(HttpHeaders.CONTENT_TYPE,
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

                    return new ResponseEntity<>(
                            application.getResume(),
                            headers,
                            HttpStatus.OK
                    );
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ================= UPDATE STATUS (ADMIN) =================
    @PutMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status,
            @RequestParam(required = false) String customMessage
    ) {

        return applicationService.findById(applicationId)
                .map(application -> {

                    application.setStatus(status);
                    applicationService.save(application);

                    User user = userService.findUser(application.getUserId());
                    if (user != null && customMessage != null && !customMessage.isBlank()) {
                        emailService.sendEmail(
                                user.getEmail(),
                                "Application Status Update",
                                "Hello " + user.getName() +
                                        ",\n\nStatus: " + status +
                                        "\nMessage: " + customMessage
                        );
                    }

                    return ResponseEntity.ok(
                            Map.of("status", "success", "application", application)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", "Application not found"))
                );
    }

    // ================= JOB STATS (ADMIN) =================
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobStatsDTO>> getJobStats() {
        return ResponseEntity.ok(applicationService.getJobStatistics());
    }
}
