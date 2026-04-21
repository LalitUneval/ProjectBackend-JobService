package com.example.studentjpa.dto.applicationdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.studentjpa.entity.company.jobApplication.ApplicationStatus;
import com.example.studentjpa.dto.user.UserResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
    
    private UserResponse userResponse;
}