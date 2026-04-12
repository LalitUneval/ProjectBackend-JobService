package com.example.studentjpa.dto.jobdto;

import com.example.studentjpa.entity.job.JobType;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJobRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotBlank(message = "City is required")
    private String city;

   
    @Positive(message = "Minimum salary must be positive")
    private Double salaryMin;

    
    @Positive(message = "Maximum salary must be positive")
    private Double salaryMax;

    
    private Boolean visaSponsored;

    @NotNull(message = "Job type is required")
    private JobType jobType;
}