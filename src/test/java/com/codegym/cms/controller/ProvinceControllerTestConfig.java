package com.codegym.cms.controller;

import com.codegym.cms.service.CustomerService;
import com.codegym.cms.service.ProvinceService;
import com.codegym.cms.service.impl.CustomerServiecImpl;
import com.codegym.cms.service.impl.ProvinceServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.codegym.cms")
public class ProvinceControllerTestConfig {

    @Bean
    public ProvinceService provinceService(){
        return Mockito.mock(ProvinceServiceImpl.class);
    }

    @Bean
    public CustomerService customerService(){
        return Mockito.mock(CustomerServiecImpl.class);
    }
}
