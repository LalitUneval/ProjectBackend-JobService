package com.example.studentjpa.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.studentjpa.dto.savejobdto.SaveJobRequest;
import com.example.studentjpa.dto.savejobdto.SavedJobResponse;
import com.example.studentjpa.repository.CompanyRepository;
import com.example.studentjpa.repository.JobRepository;
import com.example.studentjpa.repository.SavedJobRepository;

import jakarta.transaction.Transactional;

import com.example.studentjpa.entity.company.saveJob.SavedJob;
import com.example.studentjpa.entity.job.Job;

@Service
public class SaveJobService {
	
	CompanyRepository companyRepository;
    JobRepository jobRepository;
    SavedJobRepository savedJobRepository;
    SaveJobService(CompanyRepository companyRepository,JobRepository jobRepository, SavedJobRepository savedJobRepository){
		this.companyRepository=companyRepository;
		this.jobRepository=jobRepository;
		this.savedJobRepository=savedJobRepository;
	}
    
    public SavedJobResponse mapToResponse(SavedJob savedJob) {
    	return SavedJobResponse.builder()
                .id(savedJob.getId())
                .userId(savedJob.getUserId())
                .jobId(savedJob.getJob().getId())
                .jobTitle(savedJob.getJob().getTitle())
                .companyName(savedJob.getJob().getCompany().getCompanyName())
                .city(savedJob.getJob().getCity())
                .visaSponsored(savedJob.getJob().getVisaSponsored())
                .savedAt(savedJob.getSavedAt())
                .build();
    }

    @CacheEvict(value = "job_saved", key = "#request.userId")
	public SavedJobResponse saveJob(SaveJobRequest request) {
		// TODO Auto-generated method stub
		Job job = jobRepository.findById(request.getJobId())
		         .orElseThrow(() -> new RuntimeException("Job not found"));
		
	    boolean alreadySaved = savedJobRepository.existsByUserIdAndJob_Id(request.getUserId(), request.getJobId());
	    
	    if(alreadySaved) {
	    	throw new RuntimeException("Job already saved by user");
	    }
	    
	    SavedJob savedJob = SavedJob.builder()
                .userId(request.getUserId())
                .job(job)
                .build();
	    
	    savedJobRepository.save(savedJob);
	    
	    return mapToResponse(savedJob);
	}

    @Cacheable(value = "job_saved", key = "#userId")
	public List<SavedJobResponse> getUserSavedJobs(Long userId) {
		// TODO Auto-generated method stub
		List<SavedJob> response = savedJobRepository.findByUserId(userId);
		
		return response.stream()
				.map(this::mapToResponse)
				.toList();
	}
	
	//delete by savedJobId
    @CacheEvict(value = "job_saved", key = "#userId")
	@Transactional
	public void unsaveJob(Long savedJobId, Long userId) {
		// TODO Auto-generated method stub
		//this is for job is saved or not 
		SavedJob job = savedJobRepository.findById(savedJobId)
				.orElseThrow(() -> new RuntimeException("Job not found"));
		
		savedJobRepository.deleteById(savedJobId);
		
	}

    @CacheEvict(value = "job_saved", key = "#userId")
	@Transactional
	public void unsaveJobByUserAndJob(Long userId, Long jobId2) {
		// TODO Auto-generated method stub
		SavedJob job = savedJobRepository.findByUserIdAndJobId(userId, jobId2)
				.orElseThrow(() -> new RuntimeException("Job not found"));
		
		savedJobRepository.deleteByUserIdAndJobId(userId,jobId2);
	}

	public boolean isJobSaved(Long userId2, Long jobId2) {
		// TODO Auto-generated method stub
		
		return savedJobRepository.existsByUserIdAndJob_Id(userId2,jobId2);
	}
	
	

}
