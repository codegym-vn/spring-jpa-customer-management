package com.codegym.cms.controller;

import com.codegym.cms.model.Customer;
import com.codegym.cms.model.Province;
import com.codegym.cms.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

@SpringJUnitJupiterConfig(CustomerControllerTestConfig.class)
@WebAppConfiguration
class CustomerControllerTest {

    private static final String URL_CUSTOMER_LIST = "/customers";
    private static final String URL_CREATE_CUSTOMER = "/create-customer";
    private static final String URL_EDIT_CUSTOMER = "/edit-customer/{id}";
    private static final String URL_EDIT_PROVINCE_POST = "/edit-customer";
    private static final String URL_DELETE_CUSTOMER = "/delete-customer/{id}";
    private static final String URL_DELETE_CUSTOMER_POST = "/delete-customer";

    private static final String VIEW_ERROR_404 = "/error.404";
    private static final String VIEW_CUSTOMER_LIST = "/customer/list";
    private static final String VIEW_CREATE_CUSTOMER = "/customer/create";
    private static final String VIEW_EDIT_CUSTOMER = "/customer/edit";
    private static final String VIEW_DELETE_CUSTOMER = "/customer/delete";

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
    private CustomerController customerController;

    @Autowired
    CustomerService customerService;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver)
                .build();
    }

    @AfterEach
    void resetMoc(){
        Mockito.reset(customerService);
    }

    @Test
    void listCustomers() throws Exception {
        when(customerService.findAll(pageable)).thenReturn(customersPage);

        mockMvc.perform(get(URL_CUSTOMER_LIST))
                .andExpect(view().name(VIEW_CUSTOMER_LIST))
                .andExpect(model().attribute("customers", customersPage));
        verify(customerService).findAll(pageable);
    }

    @Test
    void listCustomers_Search() throws Exception {
        String s = "a";
        when(customerService.findAllByFirstNameContaining(s, pageable)).thenReturn(customersPage);

        mockMvc.perform(get(URL_CUSTOMER_LIST)
                    .param("s", s))
                .andExpect(view().name(VIEW_CUSTOMER_LIST))
                .andExpect(model().attribute("customers", customersPage));
        verify(customerService).findAllByFirstNameContaining(s, pageable);
    }

    @Test
    void showCreateForm() throws Exception {
        mockMvc.perform(get(URL_CREATE_CUSTOMER))
                .andExpect(view().name(VIEW_CREATE_CUSTOMER))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void saveCustomer_Success() throws Exception {
        mockMvc.perform(post(URL_CREATE_CUSTOMER)
                .param("firstName", firstname)
                .param("lastName", lastname))
                .andExpect(view().name(VIEW_CREATE_CUSTOMER))
                .andExpect(model().attributeExists("customer"))
                .andExpect(model().attributeExists("message"));
        verify(customerService).save(any(Customer.class));
    }

    @Test
    void showEditForm_Found() throws Exception {
        when(customerService.findById(id)).thenReturn(customer);

        mockMvc.perform(get(URL_EDIT_CUSTOMER, id))
                .andExpect(view().name(VIEW_EDIT_CUSTOMER))
                .andExpect(model().attribute("customer", customer));
        verify(customerService).findById(id);
    }

    @Test
    void showEditForm_NotFound() throws Exception {
        when(customerService.findById(id)).thenReturn(null);

        mockMvc.perform(get(URL_EDIT_CUSTOMER, id))
                .andExpect(view().name(VIEW_ERROR_404));
        verify(customerService).findById(id);
    }

    @Test
    void updateCustomer_Success() throws Exception {
        mockMvc.perform(post(URL_EDIT_PROVINCE_POST)
                .param("firstName", "new f name")
                .param("lastName", "new l name"))
                .andExpect(view().name(VIEW_EDIT_CUSTOMER))
                .andExpect(model().attributeExists("customer"))
                .andExpect(model().attributeExists("message"));
        verify(customerService).save(any(Customer.class));
    }

    @Test
    void showDeleteForm_Found() throws Exception {
        when(customerService.findById(id)).thenReturn(customer);

        mockMvc.perform(get(URL_DELETE_CUSTOMER, id))
                .andExpect(view().name(VIEW_DELETE_CUSTOMER))
                .andExpect(model().attribute("customer", customer));
        verify(customerService).findById(id);
    }

    @Test
    void showDeleteForm_NotFound() throws Exception {
        when(customerService.findById(id)).thenReturn(null);

        mockMvc.perform(get(URL_DELETE_CUSTOMER, id))
                .andExpect(view().name(VIEW_ERROR_404));
        verify(customerService).findById(id);
    }

    @Test
    void deleteCustomer_Success() throws Exception {
        mockMvc.perform(post(URL_DELETE_CUSTOMER_POST)
                .param("id", id + ""))
                .andExpect(view().name("redirect:customers"));
        verify(customerService).remove(id);
    }
}