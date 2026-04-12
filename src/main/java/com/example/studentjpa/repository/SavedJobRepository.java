package com.example.studentjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentjpa.entity.company.saveJob.SavedJob;
import com.example.studentjpa.entity.job.Job;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    
    // Find saved jobs by user ID
    List<SavedJob> findByUserId(Long userId);
    
    // Find saved jobs by job
    List<SavedJob> findByJob(Job job);
    
    // Find saved jobs by job ID
    List<SavedJob> findByJobId(Long jobId);
    
    
    // Check if user already saved a job
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    
    // Find saved job by user ID and job ID
    Optional<SavedJob> findByUserIdAndJobId(Long userId, Long jobId);
    
    // Delete saved job by user ID and job ID
    void deleteByUserIdAndJobId(Long userId, Long jobId);
    
    // Find saved jobs by user ID, ordered by saved date (most recent first)
    List<SavedJob> findByUserIdOrderBySavedAtDesc(Long userId);
    
    // Find saved jobs saved after a certain date
    List<SavedJob> findBySavedAtAfter(LocalDateTime date);
    
    // Count saved jobs by user ID
    long countByUserId(Long userId);
    
    // Count saved jobs by job ID
    long countByJobId(Long jobId);

	boolean existsByUserIdAndJob_Id(Long userId, Long jobId);

//	void deleteBysavedJobId(Long savedJobId);
}