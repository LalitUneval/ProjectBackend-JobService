package com.example.studentjpa.dto.jobdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.studentjpa.entity.job.JobType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private String description;
	private String city;
	private Double salaryMin;
	private Double salaryMax;
	private Boolean visaSponsored;
	private JobType jobType;
	private LocalDateTime postedAt;
	private Long companyId;
	private String companyName;
}