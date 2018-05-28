package com.codegym.cms.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codegym.cms.model.Customer;
import com.codegym.cms.repository.CustomerRepository;
import com.codegym.cms.service.CustomerService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;

@SpringJUnitJupiterConfig(CustomerServiceImplTestConfig.class)
public class CustomerServiceImplTest {

  static private Long id = 1L;
  static private String firstname;
  static private String lastname;
  static private Customer customer;
  static private List<Customer> customers;
  static private Page<Customer> customersPage;
  static private Page<Customer> emptyCustomersPage;
  static private Pageable pageable;

  static {
    firstname = "Foo";
    lastname = "Bar";
    customer = new Customer(firstname, lastname);
    customer.setId(id);
    customers = Arrays.asList(customer);
    customersPage = new PageImpl<>(customers);
    pageable = new PageRequest(0, 20);
  }

  private CustomerService customerService;
  private CustomerRepository customerRepository;

  @AfterEach
  public void resetAllMockedObject() {
    Mockito.reset(customerRepository);
  }

  @Test
  public void findAllWithOneCustomer() {
    when(customerRepository.findAll(pageable)).thenReturn(customersPage);
    assertEquals(customersPage, customerService.findAll(pageable));
    verify(customerRepository).findAll(pageable);
  }

  @Test
  public void findAllWithNoCustomer() {
    when(customerRepository.findAll(pageable)).thenReturn(emptyCustomersPage);
    Page<Customer> result = customerService.findAll(pageable);

    verify(customerRepository).findAll(pageable);
    assertEquals(emptyCustomersPage, result);
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
