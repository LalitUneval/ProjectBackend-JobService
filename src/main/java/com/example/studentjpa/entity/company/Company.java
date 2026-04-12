package com.example.studentjpa.entity.company;

import lombok.*;
import jakarta.persistence.*;

import java.util.List;

import com.example.studentjpa.entity.job.Job;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long createdBy;
    
    @Column(nullable = false)
    private String companyName;
    
    private String location;
    
    private String website;
    
    private String contactEmail;
    
    // One-to-Many relationship with Job
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Job> jobs;
}

