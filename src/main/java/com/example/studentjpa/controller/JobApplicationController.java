package com.example.studentjpa.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.studentjpa.client.user.UserProfileClient;
import com.example.studentjpa.dto.applicationdto.ApplyJobRequest;
import com.example.studentjpa.dto.applicationdto.JobApplicationResponse;
import com.example.studentjpa.dto.user.UserResponse;
import com.example.studentjpa.entity.company.jobApplication.ApplicationStatus;
import com.example.studentjpa.service.JobApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class JobApplicationController {
	
	private final JobApplicationService jobApplicationService;
//	private final UserProfileClient userServiceClient;
//	
//	@GetMapping("/demo")
//	public ResponseEntity<?> getDemo(@RequestHeader("Authorization") String token){
//		System.out.println("Heloo how are");
//		UserResponse userResponse = userServiceClient.getUserById(8L);
//		return ResponseEntity.ok(userResponse);
//	}
	
	//apply for job 
	@PostMapping("/{jobId}/apply")
    public ResponseEntity<JobApplicationResponse> applyForJob(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("Authorization") String token) {
        
        ApplyJobRequest request = ApplyJobRequest.builder()
                .userId(userId)
                .jobId(jobId)
                .build();
        
        JobApplicationResponse response = jobApplicationService.applyForJob(request, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
	
	//get application using applicationId
	@GetMapping("/applications/{applicationId}")
    public ResponseEntity<JobApplicationResponse> getApplication(@PathVariable Long applicationId,@RequestHeader("Authorization") String token) {
		JobApplicationResponse response = jobApplicationService.getApplication(applicationId,token);
        return ResponseEntity.ok(response);
    }
	
	//get application using userId
	@GetMapping("/applications/user/{userId}")
    public ResponseEntity<List<JobApplicationResponse>> getUserApplications(
            @PathVariable Long userId,
            @RequestHeader("X-User-Id") Long authUserId,
            @RequestHeader("Authorization") String token) {
        
        // User can only see their own applications
        if (!userId.equals(authUserId)) {
        	throw new RuntimeException("You are not authorized...");
        }
        
        List<JobApplicationResponse> applications = jobApplicationService.getUserApplications(userId,token);
        return ResponseEntity.ok(applications);
    }
	
	//Get applications for a job (Recruiter/Admin only) using jobId
	@GetMapping("/{jobId}/applications")
    public ResponseEntity<List<JobApplicationResponse>> getJobApplications(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Role") String userRole) {
        
        if (!userRole.equals("RECRUITER") && !userRole.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<JobApplicationResponse> applications = jobApplicationService.getJobApplications(jobId);
        return ResponseEntity.ok(applications);
    }
	
	//get application by satuts : now implement using status 
	@GetMapping("/applications/my-applications")
    public ResponseEntity<List<JobApplicationResponse>> getApplicationsByStatus(
            @RequestParam ApplicationStatus status,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("Authorization") String token) {
        
        List<JobApplicationResponse> applications = jobApplicationService.getApplicationsByStatus(userId, status,token);
        return ResponseEntity.ok(applications);
    }
	
	//Update application status (Recruiter/Admin only)
	@PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<JobApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status,
            @RequestHeader("X-User-Role") String userRole) {
        System.out.println("i am controlller");
        if (!userRole.equals("RECRUITER") && !userRole.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        JobApplicationResponse response = jobApplicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(response);
    }
	
	//delete application
	@DeleteMapping("/applications/delete/{applicationId}")
	public ResponseEntity<Void> withdrawApplication(@PathVariable Long applicationId,
			@RequestHeader("X-User-Id") Long userId) {

		jobApplicationService.withdrawApplication(applicationId, userId);
		return ResponseEntity.noContent().build();
	}
	
	//get job-application for recruiter 
	@GetMapping("/applications/recruiter")
	public ResponseEntity<List<JobApplicationResponse>> getApplicationsForRecruiter(
	        @RequestHeader("X-User-Id") Long recruiterId,
	        @RequestHeader("Authorization") String token
	) {
		System.out.println("RecruiterId: " + recruiterId);
	    System.out.println("Token: " + token);

	    List<JobApplicationResponse> response =
	            jobApplicationService.getApplicationsByRecruiter(recruiterId, token);

	    return ResponseEntity.ok(response);
	}

	@GetMapping("/applications/status/{status}")
	public ResponseEntity<Long> getSatusCount(
			@PathVariable String status,
			@RequestHeader("X-User-Id") Long userId) {
		Long count = jobApplicationService.getSatusCount(status, userId);
		return ResponseEntity.ok(count);
	}
}
