package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequiredInterceptor;
import com.nowcoder.interceptor.PassportInterceptor;
import com.nowcoder.interceptor.PrintRequestParamInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;
    
//    @Autowired
//    PrintRequestParamInterceptor printRequestParamInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
    	//registry.addInterceptor(printRequestParamInterceptor);
    	
        registry.addInterceptor(passportInterceptor);
        
        //只拦截/user/*下的访问
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
