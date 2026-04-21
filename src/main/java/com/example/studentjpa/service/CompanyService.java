package com.example.studentjpa.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.example.studentjpa.dto.companydto.CompanyResponse;
import com.example.studentjpa.dto.companydto.CreateCompanyRequest;
import com.example.studentjpa.dto.companydto.UpdateCompanyRequest;
import com.example.studentjpa.entity.company.Company;
import com.example.studentjpa.repository.CompanyRepository;

import jakarta.validation.Valid;

@Service
public class CompanyService {

	CompanyRepository companyRepository;
	
	CompanyService(CompanyRepository companyRepository){
		this.companyRepository=companyRepository;
	}
	
	private CompanyResponse mapToResponse(Company company) {
	    return new CompanyResponse(
	            company.getId(),
	            company.getCreatedBy(),
	            company.getCompanyName(),
	            company.getLocation(),
	            company.getWebsite(),
	            company.getContactEmail()
	    );
	}

    @Caching(evict = {
            @CacheEvict(value = "company_all", allEntries = true),
            @CacheEvict(value = "company_location", allEntries = true),
            @CacheEvict(value = "company_name", allEntries = true),
            @CacheEvict(value = "company_recruiter", allEntries = true)
    })
	public CompanyResponse createCompany(@Valid CreateCompanyRequest createCompanyDto) {
		
		if(companyRepository.existsByCompanyName(createCompanyDto.getCompanyName())) {
			throw new RuntimeException("Company is available");
		}
		
		Company company = new Company();
		company.setCreatedBy(createCompanyDto.getCreatedBy());
	    company.setCompanyName(createCompanyDto.getCompanyName());
	    company.setContactEmail(createCompanyDto.getContactEmail());
	    company.setLocation(createCompanyDto.getLocation());
	    company.setWebsite(createCompanyDto.getWebsite());

	    Company savedCompany = companyRepository.save(company);

	    return mapToResponse(savedCompany);
		
	}

    @Cacheable(value = "company_all", key = "'all'")
	public List<CompanyResponse> getAllCompanies() {
		// TODO Auto-generated method stub
		List<Company> companies=companyRepository.findAll();
		
		return companies.stream()
	            .map(this::mapToResponse)
	            .toList();
	}

    @Cacheable(value = "company_id", key = "#companyId")
	public CompanyResponse getCompany(Long companyId) {
		// TODO Auto-generated method stub
		Company company = companyRepository.findById(companyId)
	            .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

	    return mapToResponse(company);
	}

    @Cacheable(value = "company_name", key = "#name")
	public List<CompanyResponse> searchCompaniesByName(String name) {
		// TODO Auto-generated method stub
		List<Company> companies=companyRepository.findByCompanyNameContainingIgnoreCase(name);


		List<CompanyResponse> response=  companies.stream()
				.map(this::mapToResponse)
				.toList();
		return response;
	}

    @Cacheable(value = "company_location", key = "#location")
	public List<CompanyResponse> getCompaniesByLocation(String location) {
		// TODO Auto-generated method stub
        List<Company> companies=companyRepository.findByLocation(location);
		
		return companies.stream()
	            .map(this::mapToResponse)
	            .toList();
	}

    @Caching(
            put = { @CachePut(value = "company_id", key = "#companyId") },
            evict = {
                    @CacheEvict(value = "company_all", allEntries = true),
                    @CacheEvict(value = "company_location", allEntries = true),
                    @CacheEvict(value = "company_name", allEntries = true),
                    @CacheEvict(value = "company_recruiter", allEntries = true)
            }
    )
	public CompanyResponse updateCompany(Long companyId,UpdateCompanyRequest request) {
		// TODO Auto-generated method stub
		Company company = companyRepository.findById(companyId)
	            .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

	    company.setLocation(request.getLocation());
	    company.setWebsite(request.getWebsite());
	    company.setContactEmail(request.getContactEmail());

	    Company updatedCompany = companyRepository.save(company);

	    return mapToResponse(updatedCompany);
	}

    @Caching(evict = {
            @CacheEvict(value = "company_id", key = "#companyId"),
            @CacheEvict(value = "company_all", allEntries = true),
            @CacheEvict(value = "company_location", allEntries = true),
            @CacheEvict(value = "company_name", allEntries = true),
            @CacheEvict(value = "company_recruiter", allEntries = true)
    })
    public void deleteCompany(Long companyId) {
		// TODO Auto-generated method stub
		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
		
		companyRepository.delete(company);
	}

    @Cacheable(value = "company_recruiter", key = "#recruiterId")
	public List<CompanyResponse> getCompaniesByRecruiterId(Long recruiterId) {
		// TODO Auto-generated method stub
		List<Company> companies=companyRepository.findByCreatedBy(recruiterId);
		return companies.stream()
	            .map(this::mapToResponse)
	            .toList();
	}

}
