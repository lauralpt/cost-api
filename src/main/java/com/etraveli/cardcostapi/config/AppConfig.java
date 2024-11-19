package com.etraveli.cardcostapi.config;


import com.etraveli.cardcostapi.filter.RateLimiterFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final RateLimiterFilter rateLimiterFilter;

    public AppConfig(RateLimiterFilter rateLimiterFilter) {
        this.rateLimiterFilter = rateLimiterFilter;
    }

    public FilterRegistrationBean<RateLimiterFilter> rateLimiterFilterRegistration() {
        FilterRegistrationBean<RateLimiterFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(rateLimiterFilter);
        registrationBean.addUrlPatterns("/api/clearing-cost/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}

