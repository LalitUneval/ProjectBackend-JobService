package com.example.studentjpa.entity.job;



import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.example.studentjpa.entity.company.Company;
import com.example.studentjpa.entity.company.jobApplication.JobApplication;
import com.example.studentjpa.entity.company.saveJob.SavedJob;

import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    private String city;
    
    private Double salaryMin;
    
    private Double salaryMax;
    
    @Column(nullable = false)
    private Boolean visaSponsored = false;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType; // FULL_TIME, PART_TIME, CONTRACT
    
    @Column(nullable = false)
    private LocalDateTime postedAt;
    
    // Many-to-One relationship with Company
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    // One-to-Many relationship with JobApplication
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<JobApplication> applications;
    
    // One-to-Many relationship with SavedJob
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<SavedJob> savedJobs;
    
    
    @PrePersist
    public void prePersist() {
        this.postedAt = LocalDateTime.now();
    }
}
