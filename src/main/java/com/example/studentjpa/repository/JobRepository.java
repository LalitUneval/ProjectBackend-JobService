package com.example.studentjpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.studentjpa.entity.company.Company;
import com.example.studentjpa.entity.job.Job;
import com.example.studentjpa.entity.job.JobType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    // Find jobs by company
    List<Job> findByCompany(Company company);
    
    // Find jobs by company ID
    List<Job> findByCompanyId(Long companyId);
    
    // Find jobs by visa sponsorship
    List<Job> findByVisaSponsored(Boolean visaSponsored);
    
    // Find jobs by job type
    List<Job> findByJobType(JobType jobType);
    
    // Find jobs by city
    List<Job> findByCity(String city);
    
    // Find jobs by visa sponsorship and city
    List<Job> findByVisaSponsoredAndCity(Boolean visaSponsored, String city);
    
    // Find jobs by visa sponsorship and job type
    List<Job> findByVisaSponsoredAndJobType(Boolean visaSponsored, JobType jobType);
    
    // Find jobs by salary range
    @Query("SELECT j FROM Job j WHERE j.salaryMin >= :minSalary")
    List<Job> findBySalaryMinGreaterThanEqual(@Param("minSalary") Double minSalary);
    
    // Find jobs posted after a certain date
    List<Job> findByPostedAtAfter(LocalDateTime date);
    
    // Search jobs by title (case-insensitive, partial match)
    List<Job> findByTitleContainingIgnoreCase(String title);
    
    // Advanced search with multiple filters (with pagination)
    @Query("SELECT j FROM Job j WHERE " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:city IS NULL OR j.city = :city) AND " +
           "(:visaSponsored IS NULL OR j.visaSponsored = :visaSponsored) AND " +
           "(:jobType IS NULL OR j.jobType = :jobType) AND " +
           "(:minSalary IS NULL OR j.salaryMin >= :minSalary) AND " +
           "(:maxSalary IS NULL OR j.salaryMax <= :maxSalary)")
    Page<Job> searchJobs(@Param("keyword") String keyword,
                         @Param("city") String city,
                         @Param("visaSponsored") Boolean visaSponsored,
                         @Param("jobType") JobType jobType,
                         @Param("minSalary") Double minSalary,
                         @Param("maxSalary") Double maxSalary,
                         Pageable pageable);
    
    // Get latest jobs (most recent first)
    List<Job> findTop10ByOrderByPostedAtDesc();
    
    // Count jobs by company
    long countByCompanyId(Long companyId);

	List<Job> findByCompany_CreatedBy(Long recruiterId);
	
	boolean existsByIdAndTitle(Long companyId, String title);
}