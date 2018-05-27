package com.codegym.cms.service.impl;

import com.codegym.cms.model.Customer;
import com.codegym.cms.model.Province;
import com.codegym.cms.repository.CustomerRepository;
import com.codegym.cms.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitJupiterConfig(CustomerServiceImplTestConfig.class)
class CustomerServiecImplTest {

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

        emptyCustomers = new ArrayList<>();
        customers = new ArrayList<>();

        customers.add(customer);

        emptyCustomersPage = new PageImpl<>(customers);
        customersPage = new PageImpl<>(customers);

        pageable = new PageRequest(0, 20);

        province = new Province("Hanoi");
    }

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    public void resetMocks(){
        reset(customerRepository);
    }

    @Test
    void findAll_1customer() {
        when(customerRepository.findAll(pageable)).thenReturn(customersPage);
        Page<Customer> result = customerService.findAll(pageable);

        verify(customerRepository).findAll(pageable);
        assertEquals(customersPage, result);
    }

    @Test
    void findAll_0customer() {
        when(customerRepository.findAll(pageable)).thenReturn(emptyCustomersPage);
        Page<Customer> result = customerService.findAll(pageable);

        verify(customerRepository).findAll(pageable);
        assertEquals(emptyCustomersPage, result);
    }

    @Test
    void findById_Found() {
        when(customerRepository.findOne(id)).thenReturn(customer);
        Customer result = customerService.findById(id);

        verify(customerRepository).findOne(id);
        assertEquals(customer, result);
    }

    @Test
    void findById_NotFound() {
        when(customerRepository.findOne(id)).thenReturn(null);
        Customer result = customerService.findById(id);

        verify(customerRepository).findOne(id);
        assertNull(result);
    }

    @Test
    void save(){
        customerService.save(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    void remove(){
        customerService.remove(id);
        verify(customerRepository).delete(id);
    }

    @Test
    void findAllByProvince() {
        when(customerRepository.findAllByProvince(province)).thenReturn(customers);
        Iterable<Customer> result = customerService.findAllByProvince(province);

        verify(customerRepository).findAllByProvince(province);
        assertEquals(customers, result);
    }

    @Test
    void findAllByFirstNameContaining_1Customer() {
        when(customerRepository.findAllByFirstNameContaining(firstname, pageable)).thenReturn(customersPage);
        Page<Customer> result = customerService.findAllByFirstNameContaining(firstname, pageable);

        verify(customerRepository).findAllByFirstNameContaining(firstname,pageable);
        assertEquals(customersPage, result);
    }

    @Test
    void findAllByFirstNameContaining_0Customer() {
        when(customerRepository.findAllByFirstNameContaining(firstname, pageable)).thenReturn(emptyCustomersPage);
        Page<Customer> result = customerService.findAllByFirstNameContaining(firstname, pageable);

        verify(customerRepository).findAllByFirstNameContaining(firstname,pageable);
        assertEquals(emptyCustomersPage, result);
    }
}