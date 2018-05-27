package com.codegym.cms.integration;

import com.codegym.cms.model.Customer;
import com.codegym.cms.model.Province;
import com.codegym.cms.repository.CustomerRepository;
import com.codegym.cms.repository.ProvinceRepository;
import static org.hamcrest.core.StringContains.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebAppConfiguration
@SpringJUnitJupiterConfig(classes = ApplicationIntegrationTestConfig.class)
@Transactional
class CustomerControllerIntegrationTest {

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
    private static Long provinceId;
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
        province.setId(provinceId);
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    private MockMvc mockMvc;

    @Test
    void listCustomers() throws Exception {
        Province savedProvince = provinceRepository.save(province);
        customer.setProvince(savedProvince);
        customerRepository.save(customer);

        mockMvc.perform(get(URL_CUSTOMER_LIST))
                .andExpect(view().name(VIEW_CUSTOMER_LIST))
                .andExpect(model().attributeExists("customers"))
                .andExpect(result -> {
                    Page<Customer> customersPage = (Page<Customer>) result.getModelAndView().getModel().get("customers");
                    assertEquals(1, customersPage.getTotalElements());
                })
                .andExpect(content().string(containsString("<td>" + customer.getFirstName() + "</td>")))
                .andExpect(content().string(containsString("<td>" + province.getName() + "</td>")));
    }

    @Test
    void listCustomers_Empty() throws Exception {
        mockMvc.perform(get(URL_CUSTOMER_LIST))
                .andExpect(view().name(VIEW_CUSTOMER_LIST))
                .andExpect(model().attributeExists("customers"))
                .andExpect(result -> {
                    Page<Customer> customersPage = (Page<Customer>) result.getModelAndView().getModel().get("customers");
                    assertEquals(0, customersPage.getTotalElements());
                });
    }


    @Test
    void listCustomersSearch_Found() throws Exception {
        Province savedProvince = provinceRepository.save(province);
        customer.setProvince(savedProvince);
        Customer savedCustomer = customerRepository.save(customer);

        String s = "a";

        mockMvc.perform(get(URL_CUSTOMER_LIST)
                    .param("s", s))
                .andExpect(view().name(VIEW_CUSTOMER_LIST))
                .andExpect(model().attributeExists("customers"))
                .andExpect(result -> {
                    Page<Customer> customersPage = (Page<Customer>) result.getModelAndView().getModel().get("customers");
                    assertEquals(1, customersPage.getNumberOfElements());
                })
                .andExpect(content().string(containsString("<td>" + customer.getFirstName() + "</td>")))
                .andExpect(content().string(containsString("<td>" + province.getName() + "</td>")));
    }

    @Test
    void listCustomersSearch_NotFound() throws Exception {
        Province savedProvince = provinceRepository.save(province);
        customer.setProvince(savedProvince);
        customerRepository.save(customer);

        String s = "Not existed firstname";

        mockMvc.perform(get(URL_CUSTOMER_LIST)
                .param("s", s))
                .andExpect(view().name(VIEW_CUSTOMER_LIST))
                .andExpect(model().attributeExists("customers"))
                .andExpect(result -> {
                    Page<Customer> customersPage = (Page<Customer>) result.getModelAndView().getModel().get("customers");
                    assertEquals(0, customersPage.getTotalElements());
                });
    }

    @Test
    void showCreateForm() throws Exception {
        mockMvc.perform(get(URL_CREATE_CUSTOMER))
                .andExpect(view().name(VIEW_CREATE_CUSTOMER))
                .andExpect(model().attributeExists("customer"))
                .andExpect(result -> {
                    Customer customer = (Customer) result.getModelAndView().getModel().get("customer");
                    assertEquals(null, customer.getId());
                    assertEquals(null, customer.getFirstName());
                    assertEquals(null, customer.getLastName());
                });
    }

    @Test
    void saveCustomer_Success() throws Exception {
        Province savedProvince = provinceRepository.save(province);

        mockMvc.perform(post(URL_CREATE_CUSTOMER)
                .param("firstName", firstname)
                .param("lastName", lastname)
                .param("province", savedProvince.getId() + ""))
                .andExpect(view().name(VIEW_CREATE_CUSTOMER))
                .andExpect(model().attributeExists("customer"))
                .andExpect(result -> {
                    Customer customer = (Customer) result.getModelAndView().getModel().get("customer");
                    assertEquals(null, customer.getId());
                    assertEquals(null, customer.getFirstName());
                    assertEquals(null, customer.getLastName());

                    String message = (String) result.getModelAndView().getModel().get("message");
                    assertEquals("New customer created successfully", message);
                });
        Iterable<Customer> cs = customerRepository.findAll();
        Customer c = cs.iterator().next();
        assertEquals(firstname, c.getFirstName());
        assertEquals(lastname, c.getLastName());
        assertEquals(savedProvince.getId(), c.getProvince().getId());
    }

    @Test
    void showEditForm_Found() throws Exception {
        Province savedProvince = provinceRepository.save(province);
        customer.setProvince(savedProvince);
        Customer savedCustomer = customerRepository.save(customer);

        mockMvc.perform(get(URL_EDIT_CUSTOMER, savedCustomer.getId()))
                .andExpect(view().name(VIEW_EDIT_CUSTOMER))
                .andExpect(model().attribute("customer", savedCustomer));
    }

    @Test
    void showEditForm_NotFound() throws Exception {
        mockMvc.perform(get(URL_EDIT_CUSTOMER, 100000))
                .andExpect(view().name(VIEW_ERROR_404));
    }

    @Test
    void updateCustomer_Success() throws Exception {
        Province savedProvince = provinceRepository.save(province);
        customer.setProvince(savedProvince);
        Customer savedCustomer = customerRepository.save(customer);

        String newFirstName = "New first name";
        String newLastName = "New last name";
        Province newProvince = new Province("New province");
        Province newSavedProvince = provinceRepository.save(newProvince);

        mockMvc.perform(post(URL_EDIT_PROVINCE_POST)
                .param("id", savedCustomer.getId() + "")
                .param("firstName", newFirstName)
                .param("lastName", newLastName)
                .param("province", newSavedProvince.getId() + ""))
                .andExpect(view().name(VIEW_EDIT_CUSTOMER))
                .andExpect(model().attributeExists("customer"))
                .andExpect(model().attributeExists("message"));

        Customer c = customerRepository.findOne(savedCustomer.getId());
        assertEquals(newFirstName, c.getFirstName());
        assertEquals(newLastName, c.getLastName());
        assertEquals(newSavedProvince.getId(), c.getProvince().getId());
    }

    @Test
    void showDeleteForm_Found() throws Exception {
        Province savedProvince = provinceRepository.save(province);
        customer.setProvince(savedProvince);
        Customer savedCustomer = customerRepository.save(customer);

        mockMvc.perform(get(URL_DELETE_CUSTOMER, savedCustomer.getId()))
                .andExpect(view().name(VIEW_DELETE_CUSTOMER))
                .andExpect(model().attribute("customer", savedCustomer));
    }

    @Test
    void showDeleteForm_NotFound() throws Exception {
        mockMvc.perform(get(URL_DELETE_CUSTOMER, 10000))
                .andExpect(view().name(VIEW_ERROR_404));
    }

    @Test
    void deleteCustomer_Success() throws Exception {
        Province savedProvince = provinceRepository.save(province);
        customer.setProvince(savedProvince);
        Customer savedCustomer = customerRepository.save(customer);

        mockMvc.perform(post(URL_DELETE_CUSTOMER_POST)
                .param("id", savedCustomer.getId() + ""))
                .andExpect(view().name("redirect:customers"));

        Customer c = customerRepository.findOne(savedCustomer.getId());
        assertNull(c);
    }
}