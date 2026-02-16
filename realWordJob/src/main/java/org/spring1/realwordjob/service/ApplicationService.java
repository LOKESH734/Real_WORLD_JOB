package org.spring1.realwordjob.service;

import org.spring1.realwordjob.DTO.JobStatsDTO;

import org.spring1.realwordjob.model.Application;
import org.spring1.realwordjob.model.ApplicationStatus;
import org.spring1.realwordjob.model.Job;
import org.spring1.realwordjob.repository.ApplicationRepository;
import org.spring1.realwordjob.repository.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }

    public boolean existsByUserIdAndJobId(Long userId, Long jobId) {
        return applicationRepository.existsByUserIdAndJobId(userId, jobId);
    }
    public List<JobStatsDTO> getJobStatistics() {
        List<Job> jobs = jobRepo.findAll();
        List<JobStatsDTO> stats = new ArrayList<>();

        for (Job job : jobs) {
            List<Application> applications = applicationRepository.findByJobId(job.getId());

            JobStatsDTO dto = new JobStatsDTO();
            dto.setJobId(job.getId());
            dto.setJobTitle(job.getTitle());
            dto.setTotalApplications(applications.size());
            dto.setPendingApplications(applications.stream().filter(a -> a.getStatus() == ApplicationStatus.PENDING).count());
            dto.setApprovedApplications(applications.stream().filter(a -> a.getStatus() == ApplicationStatus.ACCEPTED).count());
            dto.setRejectedApplications(applications.stream().filter(a -> a.getStatus() == ApplicationStatus.REJECTED).count());

            stats.add(dto);
        }
        return stats;
    }

}
