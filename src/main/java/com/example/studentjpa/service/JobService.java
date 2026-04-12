package com.example.studentjpa.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.data.domain.*;
import com.example.studentjpa.dto.jobdto.CreateJobRequest;
import com.example.studentjpa.dto.jobdto.JobResponse;
import com.example.studentjpa.dto.jobdto.JobSearchRequest;
import com.example.studentjpa.dto.jobdto.UpdateJobRequest;
import com.example.studentjpa.entity.company.Company;
import com.example.studentjpa.entity.job.Job;
import com.example.studentjpa.entity.job.JobType;
import com.example.studentjpa.repository.CompanyRepository;
import com.example.studentjpa.repository.JobRepository;

import jakarta.validation.Valid;

@Service
//@RequiredArgsConstructor
public class JobService {
	
    CompanyRepository companyRepository;
    JobRepository jobRepository;
	
    JobService(CompanyRepository companyRepository,JobRepository jobRepository){
		this.companyRepository=companyRepository;
		this.jobRepository=jobRepository;
	}
    
    private JobResponse mapToResponse(Job job) {

        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .city(job.getCity())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .visaSponsored(job.getVisaSponsored())
                .jobType(job.getJobType())
                .postedAt(job.getPostedAt())
                .companyId(job.getCompany().getId())
                .companyName(job.getCompany().getCompanyName())
                .build();
    }
    
	public JobResponse createJob(Long companyId, @Valid CreateJobRequest request) {
		
		Company company = companyRepository.findById(companyId)
	            .orElseThrow(() ->
	                    new RuntimeException("Company not found with id: " + companyId)
	            );
		if(jobRepository.existsByIdAndTitle(companyId,request.getTitle())) {
			new RuntimeException("Job Already Created" + companyId);
		}
	    //  Create Job
	    Job job = Job.builder()
	            .title(request.getTitle())
	            .description(request.getDescription())
	            .city(request.getCity())
	            .salaryMin(request.getSalaryMin())
	            .salaryMax(request.getSalaryMax())
	            .visaSponsored(request.getVisaSponsored() != null ? request.getVisaSponsored() : false)
	            .jobType(request.getJobType())
	            .company(company)
	            .build();

	    Job savedJob = jobRepository.save(job);

	    return mapToResponse(savedJob);
	}

	public JobResponse getJob(Long jobId) {
		Job job = jobRepository.findById(jobId)
				.orElseThrow(() -> new RuntimeException("Company not found with id: " + jobId));
		
		return mapToResponse(job);
	}

	public Page<JobResponse> getAllJobs(int page, int size) {
		
		//pagination object (do not used orElseThrow() for pagination)
	    Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());

	    Page<Job> jobPage = jobRepository.findAll(pageable);
	    
	    if (jobPage.isEmpty()) {
	        throw new RuntimeException("No jobs found");
	    }
	    
	    return jobPage.map(this::mapToResponse);
	}
	
	//search service
	public Page<JobResponse> searchJobs(JobSearchRequest searchRequest) {
	
		Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), Sort.by("postedAt").descending());
		
	    Page<Job> jobPage = jobRepository.searchJobs(searchRequest.getKeyword(),
	    		searchRequest.getCity(),
	    		searchRequest.getVisaSponsored(),
	    		searchRequest.getJobType(),
	    		searchRequest.getMinSalary(),
	    		searchRequest.getMaxSalary(),
	            pageable);
	    
	    if (jobPage.isEmpty()) {
	        throw new RuntimeException("No jobs found");
	    }
	    
	    return jobPage.map(this::mapToResponse);
	 
	}

	public List<JobResponse> getJobsByCompanyId(Long companyId) {
		
		 if (!companyRepository.existsById(companyId)) {
		        throw new RuntimeException("Company not found with id: " + companyId);
		 }
		 
	    List<Job> response = jobRepository.findByCompanyId(companyId);
					
		return response.stream()
				.map(this::mapToResponse)
				.toList();
	}

	public List<JobResponse> getVisaSponsoredJobs() {
		
		List<Job> response = jobRepository.findByVisaSponsored(false);
		return response.stream()
				.map(this::mapToResponse)
				.toList();
	}

	public List<JobResponse> getJobsByCity(String city) {
		// TODO Auto-generated method stub
		List<Job> response = jobRepository.findByCity(city);
		return response.stream()
				.map(this::mapToResponse)
				.toList();
	}

	public List<JobResponse> getJobsByType(JobType jobType) {
		// TODO Auto-generated method stub
		List<Job> response = jobRepository.findByJobType(jobType);
		return response.stream()
				.map(this::mapToResponse)
				.toList();
	}

	public List<JobResponse> getLatestJobs() {
		// TODO Auto-generated method stub
		List<Job> response = jobRepository.findTop10ByOrderByPostedAtDesc();
		return response.stream()
				.map(this::mapToResponse)
				.toList();
	}

	public JobResponse updateJob(Long jobId, @Valid UpdateJobRequest request) {
		// TODO Auto-generated method stub
		Job job = jobRepository.findById(jobId)
				.orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
		job.setTitle(request.getTitle());
		job.setDescription(request.getDescription());
		job.setCity(request.getCity());
		job.setSalaryMin(request.getSalaryMin());
		job.setSalaryMax(request.getSalaryMax());
		job.setVisaSponsored(request.getVisaSponsored());
		job.setJobType(request.getJobType());
		
		Job upadatedJob = jobRepository.save(job);
		return mapToResponse(upadatedJob);
	}

	public void deleteJob(Long jobId) {
		// TODO Auto-generated method stub

		Job job = jobRepository.findById(jobId)
				.orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

		jobRepository.delete(job);
	}

	public List<JobResponse> getJobsByRecruiterId(Long recruiterId) {
		// TODO Auto-generated method stub
		List<Job> jobs = jobRepository.findByCompany_CreatedBy(recruiterId);
		
		return jobs.stream()
				.map(this::mapToResponse)
				.toList();
	}

}
