package com.example.studentjpa.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    	private Long id;
    	private String fullName;
    	private String originCountry;
    	private String currentCity;
    	private Long phoneNumber;
    	private String email;
    	
}
