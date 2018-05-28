package com.codegym.cms.controller.impl;

import com.codegym.cms.service.CustomerService;
import com.codegym.cms.service.impl.CustomerServiecImpl;
import javax.sql.DataSource;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ComponentScan("com.codegym.cms")
@EnableSpringDataWebSupport
public class CustomerControllerImplTestConfig {

  @Bean
  public CustomerService customerService() {
    return Mockito.mock(CustomerServiecImpl.class);
  }

  @Bean
  public DataSource dataSource() {
    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.H2)
        .setName("cms")
        .build();
    return db;
  }

}
