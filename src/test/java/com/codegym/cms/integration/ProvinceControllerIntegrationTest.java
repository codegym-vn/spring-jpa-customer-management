package com.codegym.cms.integration;

import com.codegym.cms.controller.ProvinceController;
import com.codegym.cms.controller.ProvinceControllerTestConfig;
import com.codegym.cms.model.Customer;
import com.codegym.cms.model.Province;
import com.codegym.cms.repository.CustomerRepository;
import com.codegym.cms.repository.ProvinceRepository;
import com.codegym.cms.service.CustomerService;
import com.codegym.cms.service.ProvinceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.ArrayList;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitJupiterConfig(ApplicationIntegrationTestConfig.class)
@WebAppConfiguration
@Transactional
class ProvinceControllerIntegrationTest {
    private static final String VIEW_ERROR_404 = "/error.404";
    private static final String VIEW_CREATE_PROVINCE = "/province/create";
    private static final String VIEW_PROVINCE_LIST = "/province/list";
    private static final String VIEW_EDIT_PROVINCE = "/province/edit";
    private static final String VIEW_DELETE_PROVINCE = "/province/delete";
    private static final String VIEW_VIEW_PROVINCE = "/province/view";

    private static final String URL_CREATE_PROVINCE = "/create-province";
    private static final String URL_PROVINCE_LIST = "/provinces";
    private static final String URL_EDIT_PROVINCE = "/edit-province/{id}";
    private static final String URL_EDIT_PROVINCE_POST = "/edit-province";
    private static final String URL_DELETE_PROVINCE = "/delete-province/{id}";
    private static final String URL_DELETE_PROVINCE_POST = "/delete-province";
    private static final String URL_VIEW_PROVINCE = "/view-province/{id}";
    static Long id;
    static Province province;
    static ArrayList<Province> provinces;
    static ArrayList<Province> emptyProvinces;
    static String provinceName = "Hanoi";
    private static Customer customer;
    private static ArrayList<Customer> customers;

    static {
        id = 1l;
        province = new Province(provinceName);
        provinces = new ArrayList<>();
        provinces.add(province);

        emptyProvinces = new ArrayList<>();
        customers = new ArrayList<>();
        customer = new Customer("A", "B");
        customers.add(customer);
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    void listProvinces() throws Exception {
        Province savedProvince = provinceRepository.save(province);

        mockMvc.perform(get(URL_PROVINCE_LIST))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_PROVINCE_LIST))
                .andExpect(model().attributeExists("provinces"))
                .andExpect(result -> {
                    Iterable<Province> provinces = (Iterable<Province>) result.getModelAndView().getModel().get("provinces");
                    Province p = provinces.iterator().next();
                    assertEquals(savedProvince.getId(), p.getId());
                })
                .andExpect(content().string(containsString(savedProvince.getName())));
    }

    @Test
    void listProvinces_Empty() throws Exception {

        mockMvc.perform(get(URL_PROVINCE_LIST))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_PROVINCE_LIST))
                .andExpect(model().attributeExists("provinces"))
                .andExpect(result -> {
                    Iterable<Province> provinces = (Iterable<Province>) result.getModelAndView().getModel().get("provinces");
                    assertFalse(provinces.iterator().hasNext());
                });
    }

    @Test
    void showCreateForm() throws Exception {
        mockMvc.perform(get(URL_CREATE_PROVINCE))
                .andExpect(view().name(VIEW_CREATE_PROVINCE))
                .andExpect(model().attributeExists("province"))
                .andExpect(result -> {
                    Province province = (Province) result.getModelAndView().getModel().get("province");
                    assertEquals(null, province.getId());
                    assertEquals(null, province.getName());
                });
    }

    @Test
    void saveProvince_Success() throws Exception {
        mockMvc.perform(post(URL_CREATE_PROVINCE)
                .param("name", provinceName))
                .andExpect(view().name(VIEW_CREATE_PROVINCE))
                .andExpect(model().attributeExists("province"))
                .andExpect(model().attributeExists("message"))
                .andExpect(result -> {
                    Province province = (Province) result.getModelAndView().getModel().get("province");
                    assertEquals(null, province.getId());
                    assertEquals(null, province.getName());

                    String message = (String) result.getModelAndView().getModel().get("message");
                    assertEquals("New province created successfully", message);
                });
        Iterable<Province> ps = provinceRepository.findAll();
        Province p = ps.iterator().next();
        assertEquals(provinceName, p.getName());
    }

    @Test
    void showEditForm_Found() throws Exception {
        Province savedProvince = provinceRepository.save(province);

        mockMvc.perform(get(URL_EDIT_PROVINCE, savedProvince.getId()))
                .andExpect(view().name(VIEW_EDIT_PROVINCE))
                .andExpect(model().attributeExists("province"))
                .andExpect(model().attribute("province", savedProvince));

    }

    @Test
    void showEditForm_NotFound() throws Exception {

        mockMvc.perform(get(URL_EDIT_PROVINCE, 10000))
                .andExpect(view().name(VIEW_ERROR_404));
    }

    @Test
    void updateProvince_Success() throws Exception {
        Province savedProvince = provinceRepository.save(province);
        String newName = "new Name";

        mockMvc.perform(post(URL_EDIT_PROVINCE_POST)
                .param("id", savedProvince.getId() + "")
                .param("name", newName))
                .andExpect(view().name(VIEW_EDIT_PROVINCE))
                .andExpect(model().attributeExists("province"))
                .andExpect(model().attributeExists("message"));
        Province p = provinceRepository.findOne(savedProvince.getId());
        assertEquals(newName, p.getName());
    }

    @Test
    void showDeleteForm_Found() throws Exception {
        Province savedProvince = provinceRepository.save(province);

        mockMvc.perform(get(URL_DELETE_PROVINCE, savedProvince.getId()))
                .andExpect(view().name(VIEW_DELETE_PROVINCE))
                .andExpect(model().attributeExists("province"))
                .andExpect(model().attribute("province", savedProvince));

    }

    @Test
    void showDeleteForm_NotFound() throws Exception {
        mockMvc.perform(get(URL_DELETE_PROVINCE, 10000))
                .andExpect(view().name(VIEW_ERROR_404));
    }

    @Test
    void deleteProvince_Success() throws Exception {
        Province savedProvince = provinceRepository.save(province);

        mockMvc.perform(post(URL_DELETE_PROVINCE_POST)
                .param("id", savedProvince.getId() + ""))
                .andExpect(view().name("redirect:provinces"));
        Province p = provinceRepository.findOne(savedProvince.getId());
        assertNull(p);
    }

    @Test
    void viewProvince() throws Exception {
        Province savedProvince = provinceRepository.save(province);
        customer.setProvince(savedProvince);
        Customer savedCustomer = customerRepository.save(customer);

        mockMvc.perform(get(URL_VIEW_PROVINCE, savedProvince.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_VIEW_PROVINCE))
                .andExpect(model().attributeExists("province"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(content().string(containsString("<h1>View province: <span>"+ savedProvince.getName() +"</span></h1>")))
                .andExpect(content().string(containsString("<td>"+ savedCustomer.getFirstName() +"</td>")));
    }

    @Test
    void viewProvince_NotFound() throws Exception {
        mockMvc.perform(get(URL_VIEW_PROVINCE, 10000))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_ERROR_404));
    }
}