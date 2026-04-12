package com.example.studentjpa.dto.companydto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;



@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyRequest {
	
//	@Id
	@NotBlank(message = "recruiter id is required")
	private Long createdBy;
	
    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Website is required")
    @Pattern(regexp = "^(https?://).*", message = "Website must start with http:// or https://")
    private String website;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Contact email is required")
    private String contactEmail;
    
}
