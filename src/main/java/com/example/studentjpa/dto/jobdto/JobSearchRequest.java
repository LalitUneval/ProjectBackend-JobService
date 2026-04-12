package com.example.studentjpa.dto.jobdto;

import com.example.studentjpa.entity.job.JobType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSearchRequest {
	private String keyword;
	private String city;
	private Boolean visaSponsored;
	private JobType jobType;
	private Double minSalary;
	private Double maxSalary;
	private Integer page;
	private Integer size;
}