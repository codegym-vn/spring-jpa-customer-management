package com.codegym.cms.controller;

import com.codegym.cms.service.CustomerService;
import com.codegym.cms.service.ProvinceService;
import com.codegym.cms.service.impl.CustomerServiecImpl;
import com.codegym.cms.service.impl.ProvinceServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

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

    @Bean
    public DataSource dataSource(){
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.H2)
                .setName("cms")
                .build();
        return db;
    }
}
