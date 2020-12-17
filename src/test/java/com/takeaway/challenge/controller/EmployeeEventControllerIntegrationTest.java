package com.takeaway.challenge.controller;

import com.takeaway.challenge.EventServiceApp;
import com.takeaway.challenge.dto.response.EmployeeEventDataResponseDto;
import com.takeaway.challenge.dto.response.GenericExceptionResponse;
import com.takeaway.challenge.exception.EmployeeEventNotFoundException;
import com.takeaway.challenge.model.EmployeeEventEntity;
import com.takeaway.challenge.repository.EmployeeEventEntityRepository;
import com.takeaway.challenge.service.impl.EmployeeAvroKafkaListenerServiceImpl;
import org.flywaydb.core.Flyway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventServiceApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class EmployeeEventControllerIntegrationTest {

    private static final String BASE_URL = "http://localhost:";
    private static final String GET_EVENT_DETAILS_URL = "/takeaway/employee-event/details?employeeId=";
    private static final String EMPLOYEE_ID = "test1";
    private static final String INVALID_EMPLOYEE_ID = "invalidtest1";
    private static final String EVENT_TYPE_CREATED = "CREATED";
    private static final String EVENT_TYPE_UPDATED = "UPDATED";
    private static final String APP_NAME = "testapp";
    private static final String ERROR = "Employee Id cannot be null or empty";

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private static final HttpHeaders HEADERS = new HttpHeaders();
    static {
        HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }

    @Autowired
    private EmployeeEventEntityRepository employeeEventEntityRepository;

    @MockBean
    private EmployeeAvroKafkaListenerServiceImpl employeeAvroKafkaListenerService;

    @MockBean
    private Flyway flyway;

    @LocalServerPort
    private Integer port;

    @Before
    @Transactional
    public void init() {

        this.employeeEventEntityRepository.deleteAll();

        this.employeeEventEntityRepository.save(
                EmployeeEventEntity.builder()
                        .employeeId(EMPLOYEE_ID)
                        .eventType(EVENT_TYPE_CREATED)
                        .appName(APP_NAME)
                        .eventTime(ZonedDateTime.now())
                        .createdAt(ZonedDateTime.now())
                        .build());

        this.employeeEventEntityRepository.save(
                EmployeeEventEntity.builder()
                        .employeeId(EMPLOYEE_ID)
                        .eventType(EVENT_TYPE_UPDATED)
                        .appName(APP_NAME)
                        .eventTime(ZonedDateTime.now())
                        .createdAt(ZonedDateTime.now())
                        .build());
    }

    @Test
    public void testGetEmployeeEventDetails() {

        // Given
        HttpEntity<Object> entity = new HttpEntity<Object>(null, HEADERS);

        ResponseEntity<EmployeeEventDataResponseDto> response = restTemplate.exchange(
                BASE_URL + port + GET_EVENT_DETAILS_URL + EMPLOYEE_ID, HttpMethod.GET, entity,
                EmployeeEventDataResponseDto.class);

        // Result
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertFalse(response.getBody().getEmployeeEvents().isEmpty());
        Assert.assertEquals(2, response.getBody().getEmployeeEvents().size());
    }

    @Test
    public void testGetEmployeeEventDetailsWhenNoDataFoundForEmployeeId() {

        // Given
        HttpEntity<Object> entity = new HttpEntity<Object>(null, HEADERS);

        ResponseEntity<GenericExceptionResponse> response = restTemplate.exchange(
                BASE_URL + port + GET_EVENT_DETAILS_URL + INVALID_EMPLOYEE_ID, HttpMethod.GET, entity,
                GenericExceptionResponse.class);

        // Result
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertFalse(response.getBody().getErrors().isEmpty());
        Assert.assertEquals(EmployeeEventNotFoundException.MESSAGE, response.getBody().getErrors().get(0));
    }

    @Test
    public void testGetEmployeeEventDetailsWhenEmployeeIdIsNull() {

        // Given
        HttpEntity<Object> entity = new HttpEntity<Object>(null, HEADERS);

        ResponseEntity<GenericExceptionResponse> response = restTemplate.exchange(
                BASE_URL + port + GET_EVENT_DETAILS_URL, HttpMethod.GET, entity,
                GenericExceptionResponse.class);

        // Result
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertFalse(response.getBody().getErrors().isEmpty());
        Assert.assertEquals(ERROR, response.getBody().getErrors().get(0));
    }
}
