package com.codegym.cms.controller.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.codegym.cms.controller.CustomerController;
import com.codegym.cms.model.Customer;
import com.codegym.cms.model.Province;
import com.codegym.cms.service.CustomerService;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringJUnitJupiterConfig(CustomerControllerImplTestConfig.class)
@WebAppConfiguration
public class CustomerControllerImplTest {


  private static Long id;
  private static String firstname = "Firstname";
  private static String lastname = "Lastname";
  private static ArrayList<Customer> customers;
  private static ArrayList<Customer> emptyCustomers;
  private static Page<Customer> customersPage;
  private static Page<Customer> emptyCustomersPage;
  private static Customer customer;
  private static Pageable pageable;
  private static Province province;

  static {
    id = 1l;
    customer = new Customer(firstname, lastname);
    customer.setId(id);
    customers = new ArrayList<>();
    customers.add(customer);
    customersPage = new PageImpl<>(customers);

    emptyCustomers = new ArrayList<>();
    emptyCustomersPage = new PageImpl<>(customers);

    pageable = new PageRequest(0, 20);
  }

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CustomerController customerController;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(customerController)
        .setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver)
        .build();
  }


  @Test
  void listCustomers() throws Exception {
    when(customerService.findAll(pageable))
        .thenReturn(customersPage);

    mockMvc
        .perform(get("/customers"))
        .andExpect(view().name("/customer/list"))
        .andExpect(model().attribute("customers", customersPage));

    verify(customerService).findAll(pageable);
  }


  @Test
  void listCustomers_Search() throws Exception {
    String s = "a";
    when(customerService.findAllByFirstNameContaining(s, pageable)).thenReturn(customersPage);

    mockMvc.perform(get("/customers")
        .param("s", s))
        .andExpect(view().name("/customer/list"))
        .andExpect(model().attribute("customers", customersPage));
    verify(customerService).findAllByFirstNameContaining(s, pageable);
  }
}
