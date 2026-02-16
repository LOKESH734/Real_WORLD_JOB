    package org.spring1.realwordjob.controller;

    import org.spring1.realwordjob.model.Job;
    import org.spring1.realwordjob.service.JobService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;
    import jakarta.validation.Valid;

    import java.util.List;

    @RestController
    @CrossOrigin(origins = "*")

    @RequestMapping("/jobs")
    public class JobController {

        @Autowired
        private JobService jobService;

        // ✅ Add Job (Admin Only)
        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping("/add")
        public ResponseEntity<Job> addJob(@Valid @RequestBody Job job) {
            return ResponseEntity.ok(jobService.addJob(job));
        }

        // ✅ Get All Jobs
        @GetMapping("/all")
        public ResponseEntity<List<Job>> getAllJobs() {
            return ResponseEntity.ok(jobService.findAll());
        }

        // ✅ Get Job by ID
        @GetMapping("/{id}")
        public ResponseEntity<Job> findJob(@PathVariable long id) {
            Job job = jobService.findJob(id);
            return (job != null) ? ResponseEntity.ok(job) : ResponseEntity.notFound().build();
        }

        // ✅ Delete Job (Admin Only)
        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<Void> deleteJob(@PathVariable long id) {
            jobService.delete(id);
            return ResponseEntity.noContent().build();
        }

        // ✅ Search Jobs with Pagination
        @GetMapping("/search")
        public ResponseEntity<Page<Job>> searchJobs(
                @RequestParam(required = false) String title,
                @RequestParam(required = false) String company,
                @RequestParam(required = false) String location,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size
        ) {
            return ResponseEntity.ok(jobService.searchJobs(title, company, location, page, size));
        }
    }
