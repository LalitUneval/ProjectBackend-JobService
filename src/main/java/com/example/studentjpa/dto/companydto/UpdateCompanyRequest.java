package com.example.studentjpa.dto.companydto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompanyRequest {

	@Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
	private String location;

	@Pattern(regexp = "^(https?://).*", message = "Website must start with http:// or https://")
	private String website;

	@Email(message = "Invalid email format")
	private String contactEmail;
}
