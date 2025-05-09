package com.hust.ittnk68.cnpm.security;

import com.hust.ittnk68.cnpm.security.filter.*;

import org.springframework.context.annotation.*;
import org.springframework.boot.web.servlet.*;

import java.util.Arrays;

@Configuration
public class FilterConfig
{

    // filter out the token in /api/service-name
    @Bean
    public FilterRegistrationBean<TokenFilter> apiTokenFilter ()
    {
        FilterRegistrationBean <TokenFilter> registrationBean = new FilterRegistrationBean<> ();
        registrationBean.setFilter (new TokenFilter ());
        registrationBean.setUrlPatterns (Arrays.asList("/api/*")); 
        registrationBean.setOrder (1);
        return registrationBean;
    }

}

