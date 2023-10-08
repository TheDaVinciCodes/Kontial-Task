package com.kontial.cloud.service.cloudservice;

import com.kontial.cloud.service.cloudservice.model.Person;
import com.kontial.cloud.service.cloudservice.persistence.InMemoryDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PersonControllerTest {

    @InjectMocks
    private PersonController personController;

    @Mock
    private InMemoryDataSource inMemoryDataSource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddValidPerson() {
        // Create a valid person
        Person validPerson = new Person("u1234", "John", LocalDate.of(1990, 5, 20));

        // Mock the behavior of the inMemoryDataSource to return true for uniqueness
        when(inMemoryDataSource.isPersonIdUnique(validPerson.id())).thenReturn(true);

        // Call the addPerson method with the valid person
        ResponseEntity<Object> response = personController.addPerson(validPerson);

        // Assert that the response status code is 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddInvalidPerson() {
        // Create an invalid person (e.g., missing name)
        Person invalidPerson = new Person("u5678", null, LocalDate.of(1990, 5, 20));

        // Mock the behavior of the inMemoryDataSource to return true for uniqueness
        when(inMemoryDataSource.isPersonIdUnique(invalidPerson.id())).thenReturn(true);

        // Call the addPerson method with the invalid person
        ResponseEntity<Object> response = personController.addPerson(invalidPerson);

        // Assert that the response status code is 400 Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Add more test cases to validate uniqueness, validation of fields, etc.
}
