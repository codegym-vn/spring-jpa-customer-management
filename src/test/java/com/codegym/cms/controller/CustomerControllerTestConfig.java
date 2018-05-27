package com.codegym.cms.controller;

import com.codegym.cms.service.CustomerService;
import com.codegym.cms.service.impl.CustomerServiecImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan("com.codegym.cms")
@EnableSpringDataWebSupport
public class CustomerControllerTestConfig {

    @Bean
    public CustomerService customerService(){
        return Mockito.mock(CustomerServiecImpl.class);
    }
}
