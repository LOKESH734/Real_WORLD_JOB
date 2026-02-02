package org.spring1.realwordjob.service;

import org.spring1.realwordjob.model.Job;
import org.spring1.realwordjob.repository.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepo repo1;

    public Job addJob(Job j) {
        return repo1.save(j);
    }

    public List<Job> findAll() {
        return repo1.findAll();
    }

    public Job findJob(long id) {
        return repo1.findById(id).orElse(null);
    }

    public void delete(long id) {
        repo1.deleteById(id);
    }

    // ✅ New method for search + pagination
    public Page<Job> searchJobs(String title, String company, String location, int page, int size) {
        return repo1.searchJobs(title, company, location, PageRequest.of(page, size));
    }
}
