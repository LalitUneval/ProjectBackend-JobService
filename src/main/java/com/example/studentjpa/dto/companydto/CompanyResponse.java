package com.example.studentjpa.dto.companydto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
    private Long id;
    private Long createdBy;
    private String companyName;
    private String location;
    private String website;
    private String contactEmail;
}
