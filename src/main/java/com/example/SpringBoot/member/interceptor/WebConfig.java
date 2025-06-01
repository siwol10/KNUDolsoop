package com.example.SpringBoot.member.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {registry.addInterceptor(new LoginCheckInterceptor())
            .addPathPatterns("/mypage/**", "/member/**", "/test") // 로그인 체크가 필요한 URL들
            .excludePathPatterns("/login","/signUp**", "/css/**", "/js/**"); // "/login 뺀 상태
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")  // URL 경로
                .addResourceLocations("file:///C:/upload/"); // 실제 디렉토리 경로 (file: 꼭 붙이기)
    }
}

