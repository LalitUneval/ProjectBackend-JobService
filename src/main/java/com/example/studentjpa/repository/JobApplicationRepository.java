package com.example.studentjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.studentjpa.entity.company.jobApplication.ApplicationStatus;
import com.example.studentjpa.entity.company.jobApplication.JobApplication;
import com.example.studentjpa.entity.job.Job;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    
    // Find applications by user ID
    List<JobApplication> findByUserId(Long userId);
    
    // Find applications by job
    List<JobApplication> findByJob(Job job);
    
    // Find applications by job ID
    List<JobApplication> findByJobId(Long jobId);
    
    // Find applications by status
    List<JobApplication> findByStatus(ApplicationStatus status);
    
    // Find applications by user ID and status
    List<JobApplication> findByUserIdAndStatus(Long userId, ApplicationStatus status);
    
    // Find applications by job ID and status
    List<JobApplication> findByJobIdAndStatus(Long jobId, ApplicationStatus status);
    
    // Check if user already applied to a job
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    
    // Find application by user ID and job ID
    Optional<JobApplication> findByUserIdAndJobId(Long userId, Long jobId);
    
    // Find applications applied after a certain date
    List<JobApplication> findByAppliedAtAfter(LocalDateTime date);
    
    // Count applications by user ID
    long countByUserId(Long userId);
    
    // Count applications by job ID
    long countByJobId(Long jobId);
    
    // Count applications by status
    long countByStatus(ApplicationStatus status);
    
    // Get user's applications ordered by date (most recent first)
    List<JobApplication> findByUserIdOrderByAppliedAtDesc(Long userId);
    
    // Get applications for a job ordered by date
    List<JobApplication> findByJobIdOrderByAppliedAtDesc(Long jobId);

	List<JobApplication> findByJob_Company_CreatedBy(Long recruiterId);

    @Query("SELECT COUNT(a) FROM JobApplication a WHERE a.userId = :userId AND a.status = :status")
    Long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") ApplicationStatus status);


}
