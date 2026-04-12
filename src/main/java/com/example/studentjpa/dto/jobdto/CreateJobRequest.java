package com.example.studentjpa.dto.jobdto;

import com.example.studentjpa.entity.job.JobType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobRequest {

	@NotBlank(message = "Job title is required")
	private String title;

	@NotBlank(message = "Job description is required")
	private String description;
	
	@NotBlank(message = "Job Lcation is required")
	private String city;
	
	@Positive(message = "Minimum salary must be positive")
	private Double salaryMin;
	
	@Positive(message = "Maximum salary must be positive")
	private Double salaryMax;
	
	
	private Boolean visaSponsored;

	@NotNull(message = "Job type is required")
	private JobType jobType;

//	@NotNull(message = "Company ID is required")
//	private Long companyId;
}