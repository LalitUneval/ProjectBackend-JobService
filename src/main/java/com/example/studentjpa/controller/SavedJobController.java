package com.example.studentjpa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.studentjpa.dto.savejobdto.SaveJobRequest;
import com.example.studentjpa.dto.savejobdto.SavedJobResponse;
import com.example.studentjpa.service.JobService;
import com.example.studentjpa.service.SaveJobService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/jobs")
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class SavedJobController {
	
	private final SaveJobService saveJobService;
	
	//save job
	@PostMapping("/{jobId}/save")
	public ResponseEntity<SavedJobResponse> saveJob(
			 @PathVariable Long jobId,
	         @RequestHeader("X-User-Id") Long userId){
		
		SaveJobRequest request = SaveJobRequest.builder()
                .userId(userId)
                .jobId(jobId)
                .build();
        
        SavedJobResponse response = saveJobService.saveJob(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	//get saved job using userId
	@GetMapping("/saved/user/{userId}")
    public ResponseEntity<List<SavedJobResponse>> getUserSavedJobs(
            @PathVariable Long userId,
            @RequestHeader("X-User-Id") Long authUserId) {
        
        // User can only see their own saved jobs
        if (!userId.equals(authUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<SavedJobResponse> savedJobs = saveJobService.getUserSavedJobs(userId);
        return ResponseEntity.ok(savedJobs);
    }
	
	//remove job from saveJob table using savedJobId
	@DeleteMapping("/saved/{savedJobId}")
    public ResponseEntity<Void> unsaveJob(
            @PathVariable Long savedJobId,
            @RequestHeader("X-User-Id") Long userId) {
        
		saveJobService.unsaveJob(savedJobId, userId);
        return ResponseEntity.noContent().build();
    }
	
	//remove job from saveJob table using jobId & userId
	@DeleteMapping("/{jobId}/unsave")
    public ResponseEntity<Void> unsaveJobByUserAndJob(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Id") Long userId) {
        
		saveJobService.unsaveJobByUserAndJob(userId, jobId);
        return ResponseEntity.noContent().build();
    }
	
	//check job saved or not
	@GetMapping("/{jobId}/is-saved")
    public ResponseEntity<Boolean> isJobSaved(
            @PathVariable Long jobId,
            @RequestHeader("X-User-Id") Long userId) {
        
        boolean isSaved = saveJobService.isJobSaved(userId, jobId);
        return ResponseEntity.ok(isSaved);
    }
	
}
