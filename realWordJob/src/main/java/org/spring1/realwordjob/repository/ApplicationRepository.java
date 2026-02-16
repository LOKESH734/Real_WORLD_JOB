package org.spring1.realwordjob.repository;

import org.spring1.realwordjob.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    // ✅ Add this method to get applications by jobId
    List<Application> findByJobId(Long jobId);
}
