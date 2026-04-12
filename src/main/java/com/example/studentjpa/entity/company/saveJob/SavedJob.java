package com.example.studentjpa.entity.company.saveJob;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.example.studentjpa.entity.job.Job;


@Entity
@Table(name = "saved_jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJob {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Foreign key to UserProfile.id (cross-service reference)
    @Column(nullable = false)
    private Long userId;
    
    // Many-to-One relationship with Job
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    @Column(nullable = false)
    private LocalDateTime savedAt; 
    
    @PrePersist
    public void prePersist() {
        this.savedAt = LocalDateTime.now();
    }


}

