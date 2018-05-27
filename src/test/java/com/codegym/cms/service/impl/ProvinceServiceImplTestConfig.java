package com.codegym.cms.service.impl;

import com.codegym.cms.repository.ProvinceRepository;
import com.codegym.cms.service.ProvinceService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProvinceServiceImplTestConfig {

    @Bean
    public ProvinceService provinceService(){
        return new ProvinceServiceImpl();
    }

    @Bean
    public ProvinceRepository provinceRepository(){
        return Mockito.mock(ProvinceRepository.class);
    }
}
