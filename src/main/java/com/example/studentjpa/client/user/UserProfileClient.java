package com.example.studentjpa.client.user;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import com.example.studentjpa.dto.user.UserResponse;


@FeignClient(name = "user-service")
public interface UserProfileClient {

    @GetMapping("/api/users/profile/{userId}")
    UserResponse getUserById(@PathVariable ("userId") Long userId);

}