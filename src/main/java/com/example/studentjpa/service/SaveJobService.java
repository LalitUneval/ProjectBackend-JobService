package com.example.studentjpa.service;

import java.time.LocalDateTime;
import java.util.List;

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

	public List<SavedJobResponse> getUserSavedJobs(Long userId2) {
		// TODO Auto-generated method stub
		List<SavedJob> response = savedJobRepository.findByUserId(userId2);
		
		return response.stream()
				.map(this::mapToResponse)
				.toList();
	}
	
	//delete by savedJobId
	@Transactional
	public void unsaveJob(Long savedJobId, Long userId2) {
		// TODO Auto-generated method stub
		//this is for job is saved or not 
		SavedJob job = savedJobRepository.findById(savedJobId)
				.orElseThrow(() -> new RuntimeException("Job not found"));
		
		savedJobRepository.deleteById(savedJobId);
		
	}
	
	@Transactional
	public void unsaveJobByUserAndJob(Long userId2, Long jobId2) {
		// TODO Auto-generated method stub
		SavedJob job = savedJobRepository.findByUserIdAndJobId(userId2, jobId2)
				.orElseThrow(() -> new RuntimeException("Job not found"));
		
		savedJobRepository.deleteByUserIdAndJobId(userId2,jobId2);
	}

	public boolean isJobSaved(Long userId2, Long jobId2) {
		// TODO Auto-generated method stub
		
		return savedJobRepository.existsByUserIdAndJob_Id(userId2,jobId2);
	}
	
	

}
