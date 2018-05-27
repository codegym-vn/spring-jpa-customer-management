package com.codegym.cms.service.impl;

import com.codegym.cms.model.Province;
import com.codegym.cms.repository.ProvinceRepository;
import com.codegym.cms.service.ProvinceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitJupiterConfig(ProvinceServiceImplTestConfig.class)
class ProvinceServiceImplTest {

    static Long id;
    static Province province;
    static ArrayList<Province> provinces;
    static ArrayList<Province> emptyProvinces;

    static {
        id = 1l;
        province = new Province("Hanoi");
        provinces = new ArrayList<>();
        provinces.add(province);

        emptyProvinces = new ArrayList<>();
    }

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private ProvinceRepository provinceRepository;

    @AfterEach
    public void resetMocks(){
        reset(provinceRepository);
    }

    @Test
    void findAll_1Customer() {
        when(provinceRepository.findAll()).thenReturn(provinces);
        Iterable<Province> result = provinceService.findAll();

        verify(provinceRepository).findAll();
        assertEquals(provinces, result);
    }

    @Test
    void findAll_0Customer() {
        when(provinceRepository.findAll()).thenReturn(emptyProvinces);
        Iterable<Province> result = provinceService.findAll();

        verify(provinceRepository).findAll();
        assertEquals(emptyProvinces, result);
    }

    @Test
    void findById_Found() {
        when(provinceRepository.findOne(id)).thenReturn(province);
        Province result = provinceService.findById(id);

        verify(provinceRepository).findOne(id);
        assertEquals(province, result);
    }

    @Test
    void findById_NotFound() {
        when(provinceRepository.findOne(id)).thenReturn(null);
        Province result = provinceService.findById(id);

        verify(provinceRepository).findOne(id);
        assertNull(result);
    }

    @Test
    void save() {
        provinceService.save(province);
        verify(provinceRepository).save(province);
    }

    @Test
    void remove() {
        provinceService.remove(id);
        verify(provinceRepository).delete(id);
    }
}