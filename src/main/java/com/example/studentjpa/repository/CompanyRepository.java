package com.example.studentjpa.repository;

import com.example.studentjpa.dto.companydto.CompanyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentjpa.entity.company.Company;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    
    // Find company by name
    Optional<Company> findByCompanyName(String companyName);
    
    // Check if company name exists
    boolean existsByCompanyName(String companyName);
    
    // Find companies by location
    List<Company> findByLocation(String location);
    
    // Search companies by name (case-insensitive, partial match)
    List<Company> findByCompanyNameContainingIgnoreCase(String companyName);
    
    // Find companies by location containing keyword
    List<Company> findByLocationContainingIgnoreCase(String location);
    
    List<Company> findByCreatedBy(Long userId);

	
    
}