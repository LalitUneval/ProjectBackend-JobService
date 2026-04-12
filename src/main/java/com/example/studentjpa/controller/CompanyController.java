
package com.example.studentjpa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.example.studentjpa.dto.companydto.CompanyResponse;
import com.example.studentjpa.dto.companydto.CreateCompanyRequest;
import com.example.studentjpa.dto.companydto.UpdateCompanyRequest;
import com.example.studentjpa.service.CompanyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/jobs/companies")
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class CompanyController {

	private final CompanyService companyService;

	//create new company
	@PostMapping("/add")
	public ResponseEntity<CompanyResponse> createCompany(
	        @RequestBody CreateCompanyRequest createCompanyDto,
	        @RequestHeader(value = "X-User-Role", required = false) String userRole,
	        @RequestHeader(value = "X-User-Id", required = false) Long recruiterId) {

	    if (userRole == null ||
	        (!userRole.equals("RECRUITER") && !userRole.equals("ADMIN"))) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	    }
	    
	    createCompanyDto.setCreatedBy(recruiterId);
	    
	    CompanyResponse response = companyService.createCompany(createCompanyDto);
	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	//get all company
	@GetMapping("/all")
	public ResponseEntity<List<CompanyResponse>> getAllCompanies(){
		List<CompanyResponse> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
	}
	
	//get company using companyId
	@GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable Long companyId) {
        CompanyResponse response = companyService.getCompany(companyId);
        return ResponseEntity.ok(response);
    }
	
	//Search companies by name
	@GetMapping("/search")
    public ResponseEntity<List<CompanyResponse>> searchCompanies(@RequestParam String name) {
		List<CompanyResponse> companies = companyService.searchCompaniesByName(name);
        return ResponseEntity.ok(companies);
    }
	
	//Get companies by location
	@GetMapping("/location/{location}")
    public ResponseEntity<List<CompanyResponse>> getCompaniesByLocation(@PathVariable String location) {
        List<CompanyResponse> companies = companyService.getCompaniesByLocation(location);
        return ResponseEntity.ok(companies);
    }
	
	//Get companies by recruiterId
		@GetMapping("/recruiter")
	    public ResponseEntity<List<CompanyResponse>> getCompaniesByuserId(
	    		@RequestHeader(value = "X-User-Role", required = false) String userRole,
		        @RequestHeader(value = "X-User-Id", required = false) Long recruiterId){
		        	
		        	if (userRole == null ||
		        	        (!userRole.equals("RECRUITER") && !userRole.equals("ADMIN"))) {
		        	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		        	    }
		        	
	        List<CompanyResponse> companies = companyService.getCompaniesByRecruiterId(recruiterId);
	        return ResponseEntity.ok(companies);
	    }
	
	//Update company
	@PutMapping("/{companyId}")
	public ResponseEntity<CompanyResponse> updateCompany(
	        @PathVariable Long companyId,
	        @Valid @RequestBody UpdateCompanyRequest request,
	        @RequestHeader(value = "X-User-Role", required = false) String userRole) {
		
		if (userRole == null ||
		        (!userRole.equals("RECRUITER") && !userRole.equals("ADMIN"))) {
		        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
	    CompanyResponse response = companyService.updateCompany(companyId, request);
	    return ResponseEntity.ok(response);
	}
	
	//Delete company
	@DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.noContent().build();
    }
}
