package com.example.studentjpa.service;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.studentjpa.client.user.UserProfileClient;

import com.example.studentjpa.dto.applicationdto.ApplyJobRequest;
import com.example.studentjpa.dto.applicationdto.JobApplicationResponse;
import com.example.studentjpa.dto.user.UserResponse;
import com.example.studentjpa.entity.company.jobApplication.ApplicationStatus;
import com.example.studentjpa.entity.company.jobApplication.JobApplication;
import com.example.studentjpa.entity.company.saveJob.SavedJob;
import com.example.studentjpa.entity.job.Job;
import com.example.studentjpa.repository.JobRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.example.studentjpa.repository.JobApplicationRepository;

@Service
@Slf4j
public class JobApplicationService {
	
	JobRepository jobRepository;
	JobApplicationRepository jobApplicationRepository;
	UserProfileClient userServiceClient;
	JobApplicationService(JobRepository jobRepository,JobApplicationRepository jobApplicationRepository, UserProfileClient userServiceClient){
		this.jobRepository=jobRepository;
		this.jobApplicationRepository=jobApplicationRepository;
		this.userServiceClient=userServiceClient;
	}
	
	public JobApplicationResponse mapToResponse(JobApplication savedApplication,UserResponse userResponse) {
		return JobApplicationResponse.builder()
                .id(savedApplication.getId())
                .userId(savedApplication.getUserId())
                .jobId(savedApplication.getJob().getId())
                .jobTitle(savedApplication.getJob().getTitle())
                .companyName(savedApplication.getJob().getCompany().getCompanyName())
                .status(savedApplication.getStatus())
                .updatedAt(savedApplication.getUpdatedAt())
                .appliedAt(savedApplication.getAppliedAt())
                .userResponse(userResponse)
                .build();
	}
	
	public JobApplicationResponse mapToResponse2(JobApplication savedApplication) {
		return JobApplicationResponse.builder()
                .id(savedApplication.getId())
                .userId(savedApplication.getUserId())
                .jobId(savedApplication.getJob().getId())
                .jobTitle(savedApplication.getJob().getTitle())
                .companyName(savedApplication.getJob().getCompany().getCompanyName())
                .status(savedApplication.getStatus())
                .updatedAt(savedApplication.getUpdatedAt())
                .appliedAt(savedApplication.getAppliedAt())
                .build();
	}

    @CacheEvict(value = "job_application_user", key = "#request.userId")
	public JobApplicationResponse applyForJob(ApplyJobRequest request , String Token) {
		// TODO Auto-generated method stub
		Job job= jobRepository.findById(request.getJobId())
		                .orElseThrow(() -> new RuntimeException("Job not found"));
		
		boolean  alreadySaved= jobApplicationRepository.existsByUserIdAndJobId(request.getUserId(),request.getJobId());
	    
		if(alreadySaved) {
	    	throw new RuntimeException("You already applied");
	    }
		
		UserResponse userResponse = userServiceClient.getUserById(request.getUserId());
		
		JobApplication jobApplication = JobApplication.builder()
                .userId(request.getUserId())
                .job(job)
                .build();
		
		JobApplication savedApplication = jobApplicationRepository.save(jobApplication);
		
		
		return mapToResponse(savedApplication,userResponse);
	}

	
	public JobApplicationResponse getApplication(Long applicationId,String Token) {
		// TODO Auto-generated method stub
		JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new RuntimeException("Job Application not found"));
		
		UserResponse userResponse = userServiceClient.getUserById(jobApplication.getUserId());
		return mapToResponse(jobApplication,userResponse);
	}

    @Cacheable(value = "job_application_user", key = "#userId")
	public List<JobApplicationResponse> getUserApplications(Long userId,String Token) {
		// TODO Auto-generated method stub
		List<JobApplication> jobApplications= jobApplicationRepository.findByUserId(userId);
		
		UserResponse userResponse = userServiceClient.getUserById(userId);
		log.info("User response from the user {} "+userResponse);
		
		return jobApplications.stream()
				.map(request -> mapToResponse(request, userResponse))
				.toList();
	}

	public List<JobApplicationResponse> getJobApplications(Long jobId) {
		// TODO Auto-generated method stub
        List<JobApplication> jobApplications= jobApplicationRepository.findByJobId(jobId);
		
//    	UserResponse userResponse = userServiceClient.getUserById(jobApplications.getUserId());
    	
		return jobApplications.stream()
				.map(this:: mapToResponse2)
				.toList();
	}

	public List<JobApplicationResponse> getApplicationsByStatus(Long userId, ApplicationStatus status, String Token) {
		// TODO Auto-generated method stub
        List<JobApplication> jobApplications= jobApplicationRepository.findByStatus(status);
		
//        UserResponse userResponse = userServiceClient.getUserById(userId);
        UserResponse userResponse = userServiceClient.getUserById(userId);
		return jobApplications.stream()
				.map(request -> mapToResponse(request, userResponse))
				.toList();
	}

	public JobApplicationResponse updateApplicationStatus(Long applicationId, ApplicationStatus status) {
		// TODO Auto-generated method stub
		JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new RuntimeException("Job Application not found"));
		
		jobApplication.setStatus(status);
		jobApplication.setUpdatedAt(LocalDateTime.now());
		JobApplication response =jobApplicationRepository.save(jobApplication);
		return mapToResponse2(response);
	}

	public Long getSatusCount(String status, Long userId) {
		// TODO Auto-generated method stub
		ApplicationStatus appStatus = ApplicationStatus.valueOf(status.toUpperCase());
		return jobApplicationRepository.countByUserIdAndStatus(userId, appStatus);
	}

    @CacheEvict(value = "job_application_user", key = "#userId")
	@Transactional
	public void withdrawApplication(Long applicationId, Long userId) {
		// TODO Auto-generated method stub
		JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
				.orElseThrow(() -> new RuntimeException("Job Application not found"));
		jobApplicationRepository.delete(jobApplication);
	}

    @Cacheable(value = "job_application_recruiter", key = "#recruiterId")
	public List<JobApplicationResponse> getApplicationsByRecruiter(Long recruiterId, String token) {
		
		System.out.println("RecruiterId: " + recruiterId);
	    System.out.println("Token: " + token);
	    
	    List<JobApplication> applications =
	        jobApplicationRepository.findByJob_Company_CreatedBy(recruiterId);

	    return applications.stream()
	    		.map(app -> {

	    	        UserResponse user = null;

	    	        try {
	    	            user = userServiceClient.getUserById(app.getUserId());
	    	        } catch (Exception e) {
	    	            System.out.println("User not found: " + app.getUserId());
	    	        }

	    	        return mapToResponse(app, user);
	    	    })
	            .toList();
	}



}
