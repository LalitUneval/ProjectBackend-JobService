package com.example.studentjpa.dto.companydto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long createdBy;
    private String companyName;
    private String location;
    private String website;
    private String contactEmail;
}
