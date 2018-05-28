package com.codegym.cms.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codegym.cms.repository.CustomerRepository;
import com.codegym.cms.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;

@SpringJUnitJupiterConfig(CustomerServiceImplTestConfig.class)
public class CustomerServiceImplTest {

  static private Pageable pageable = new PageRequest(0, 20);

  private CustomerService customerService;
  private CustomerRepository customerRepository;

  @AfterEach
  public void resetAllMockedObject() {
    Mockito.reset(customerRepository);
  }

  @Test
  public void findAllWithOneCustomer() {
    when(customerRepository.findAll(pageable)).thenReturn(null);
    customerService.findAll(pageable);
    verify(customerRepository).findAll(pageable);
  }

  @Autowired
  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }

  @Autowired
  public void setCustomerRepository(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }
}
