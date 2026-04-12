package com.example.studentjpa.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component  // ← THIS MUST BE HERE - Feign needs it as a Spring bean
public class FeignAuthInterceptor implements RequestInterceptor {

    
    private String internalKey="Lalit-Super-Secret-Key-2026";

    @Override
    public void apply(RequestTemplate template) {
        // Send internal key so user-service allows the request
        template.header("X-Internal-Secret", internalKey);

        // Also forward JWT if available
        String token = UserTokenHolder.getToken();
        if (token != null) {
            template.header("Authorization", token);
        }
    }
}