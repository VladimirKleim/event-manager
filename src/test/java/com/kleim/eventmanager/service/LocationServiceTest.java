package com.kleim.eventmanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kleim.eventmanager.entity.LocationEntity;
import com.kleim.eventmanager.model.Location;
import com.kleim.eventmanager.model.LocationConverter;
import com.kleim.eventmanager.model.LocationDto;
import com.kleim.eventmanager.model.LocationEntityConverter;
import com.kleim.eventmanager.repository.LocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LocationServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private  ObjectMapper objectMapper;


    @Test
    void createLocate() throws Exception {
        var location = new Location(
                null,
                "some-body",
                "yl.Krasivaya",
                3000,
                "None"
        );
        String locationJson = objectMapper.writeValueAsString(location);
        String MockLocationJson = mockMvc.perform(post("/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(locationJson))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Location createdLocation = objectMapper.readValue(MockLocationJson, Location.class);
        Assertions.assertEquals(location.address(), createdLocation.address());
        Assertions.assertEquals(location.capacity(), createdLocation.capacity());
        Assertions.assertEquals(location.name(), createdLocation.name());
    }

    @Test
    void getLocationById() {
    }
}