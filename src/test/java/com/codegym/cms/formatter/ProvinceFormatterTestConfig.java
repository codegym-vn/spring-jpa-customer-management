package com.codegym.cms.formatter;

import com.codegym.cms.repository.ProvinceRepository;
import com.codegym.cms.service.ProvinceService;
import com.codegym.cms.service.impl.ProvinceServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProvinceFormatterTestConfig {

    @Bean
    public ProvinceService provinceService(){
        return Mockito.mock(ProvinceServiceImpl.class);
    }

    @Bean
    public ProvinceRepository provinceRepository(){
        return Mockito.mock(ProvinceRepository.class);
    }
}
