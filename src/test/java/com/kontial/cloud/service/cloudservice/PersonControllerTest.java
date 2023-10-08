package com.kontial.cloud.service.cloudservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kontial.cloud.service.cloudservice.PersonController;
import com.kontial.cloud.service.cloudservice.model.Person;
import com.kontial.cloud.service.cloudservice.persistence.InMemoryDataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InMemoryDataSource inMemoryDataSource;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper to convert objects to JSON

    @Before
    public void setUp() {
        // Initialize your test data
    }

    @Test
    public void testGetPersonsSummary() throws Exception {
        // Create a list of sample persons
        List<Person> persons = Arrays.asList(
                new Person("1", "John Doe", LocalDate.of(1990, 5, 15)),
                new Person("2", "Jane Smith", LocalDate.of(1985, 8, 25)),
                new Person("3", "Alice Johnson", LocalDate.of(1988, 3, 10))
        );

        // Mock the inMemoryDataSource's getAll() method to return the sample persons
        when(inMemoryDataSource.getAll()).thenReturn(persons);

        // Expected summary map sorted by name in ascending order
        Map<String, Integer> expectedSummary = new TreeMap<>();
        expectedSummary.put("Alice Johnson", 1);
        expectedSummary.put("Jane Smith", 1);
        expectedSummary.put("John Doe", 1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/persons/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedSummary)));
    }

    @Test
    public void testAddPerson() throws Exception {
        // Create a sample person
        Person newPerson = new Person("3", "Alice Johnson", LocalDate.of(1988, 3, 10));

        // Mock the inMemoryDataSource's methods for validation and ID uniqueness
        when(inMemoryDataSource.isValidPerson(any(Person.class))).thenReturn(true);
        when(inMemoryDataSource.isPersonIdUnique("3")).thenReturn(true);

        // Serialize the new person object to JSON
        String jsonNewPerson = objectMapper.writeValueAsString(newPerson);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNewPerson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Add more test methods for other controller endpoints as needed
}
