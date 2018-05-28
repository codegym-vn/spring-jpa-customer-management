package com.codegym.cms.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.codegym.cms.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;

@SpringJUnitJupiterConfig(CustomerServiceImplTestConfig.class)
public class CustomerServiceImplTest {

  private CustomerService customerService;

  @Test
  public void findAllWithOneCustomer() {
    assertNotNull(customerService);
  }

  @Autowired
  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }
}
