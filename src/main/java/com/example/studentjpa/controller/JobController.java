package com.example.studentjpa.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.studentjpa.dto.jobdto.CreateJobRequest;
import com.example.studentjpa.dto.jobdto.JobResponse;
import com.example.studentjpa.dto.jobdto.JobSearchRequest;
import com.example.studentjpa.dto.jobdto.UpdateJobRequest;
import com.example.studentjpa.entity.job.JobType;
import com.example.studentjpa.service.CompanyService;
import com.example.studentjpa.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/jobs")
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class JobController {
	
	private final JobService jobService;
	
	//create job 
	@PostMapping("/{companyId}")
    public ResponseEntity<JobResponse> createJob(
    		@PathVariable Long companyId,
            @Valid @RequestBody CreateJobRequest request,
            @RequestHeader("X-User-Role") String userRole) {
        
        if (!userRole.equals("RECRUITER") && !userRole.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        JobResponse response = jobService.createJob(companyId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
	
	//get job using jobId
	@GetMapping("{jobId}")
    public ResponseEntity<JobResponse> getJob(@PathVariable Long jobId) {
        JobResponse response = jobService.getJob(jobId);
        return ResponseEntity.ok(response);
    }
	
	//get all jobs
	@GetMapping("/all")
    public ResponseEntity<Page<JobResponse>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue =
            "10") int size) {
        
        Page<JobResponse> jobs = jobService.getAllJobs(page, size);
        
        return ResponseEntity.ok(jobs);
    }
	
	//search jobs : keyword is used for title search
	@GetMapping("/search")
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Boolean visaSponsored,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        JobSearchRequest searchRequest = JobSearchRequest.builder()
                .keyword(keyword)
                .city(city)
                .visaSponsored(visaSponsored)
                .jobType(jobType)
                .minSalary(minSalary)
                .maxSalary(maxSalary)
                .page(page)
                .size(size)
                .build();
        
        Page<JobResponse> jobs = jobService.searchJobs(searchRequest);
        return ResponseEntity.ok(jobs);
    }
    
	//get job using companyId
	@GetMapping("/company/{companyId}")
	public ResponseEntity<List<JobResponse>> getJobsByCompanyId(
	        @PathVariable Long companyId) {

	    List<JobResponse> jobs = jobService.getJobsByCompanyId(companyId);
	    return ResponseEntity.ok(jobs);
	}
	
	//get jobs using recruiterId
	@GetMapping("/recruiter/jobs")
	public ResponseEntity<List<JobResponse>> getJobsByCompanyId(
			@RequestHeader(value = "X-User-Role", required = false) String userRole,
	        @RequestHeader(value = "X-User-Id", required = false) Long recruiterId) {
		
		if (!userRole.equals("RECRUITER") && !userRole.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
		
	    List<JobResponse> jobs = jobService.getJobsByRecruiterId(recruiterId);
	    return ResponseEntity.ok(jobs);
	}
	
	//get job using visa sponsored type : it is static, for visa sponsored are get
	@GetMapping("/visa-sponsored")
	public ResponseEntity<List<JobResponse>> getVisaSponsoredJobs() {

	    List<JobResponse> jobs = jobService.getVisaSponsoredJobs();
	    
	    return ResponseEntity.ok(jobs);
	}
	
	//get job using city
	@GetMapping("/city/{city}")
    public ResponseEntity<List<JobResponse>> getJobsByCity(@PathVariable String city) {
        List<JobResponse> jobs = jobService.getJobsByCity(city);
        return ResponseEntity.ok(jobs);
    }
	
	//get job using job-type
	@GetMapping("/type/{jobType}")
    public ResponseEntity<List<JobResponse>> getJobsByType(@PathVariable JobType jobType) {
        List<JobResponse> jobs = jobService.getJobsByType(jobType);
        return ResponseEntity.ok(jobs);
    }
	
	//get latest jobs
	@GetMapping("/latest")
    public ResponseEntity<List<JobResponse>> getLatestJobs() {
        List<JobResponse> jobs = jobService.getLatestJobs();
        return ResponseEntity.ok(jobs);
    }
	
	//update job
	@PutMapping("/{jobId}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody UpdateJobRequest request,
            @RequestHeader("X-User-Role") String userRole) {
        
        if (!userRole.equals("RECRUITER") && !userRole.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        JobResponse response = jobService.updateJob(jobId, request);
        return ResponseEntity.ok(response);
    }
	
	@DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Role") String userRole) {
        
		if (!userRole.equals("RECRUITER") && !userRole.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }
}
