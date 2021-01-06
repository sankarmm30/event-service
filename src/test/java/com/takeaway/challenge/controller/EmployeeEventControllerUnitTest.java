package com.takeaway.challenge.controller;

import com.takeaway.challenge.EmployeeEventType;
import com.takeaway.challenge.dto.response.EmployeeEvent;
import com.takeaway.challenge.dto.response.EmployeeEventDataResponseDto;
import com.takeaway.challenge.exception.EmployeeEventNotFoundException;
import com.takeaway.challenge.exception.TakeAwayClientRuntimeException;
import com.takeaway.challenge.exception.TakeAwayServerRuntimeException;
import com.takeaway.challenge.exception.advice.GenericExceptionHandlerAdvice;
import com.takeaway.challenge.service.EmployeeEventService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Collections;

@RunWith(JUnit4.class)
public class EmployeeEventControllerUnitTest {

    private static final String EMPLOYEE_ID = "test";
    private static final String URL = "/employee-event/details?employeeId=";

    @Mock
    private EmployeeEventService employeeEventService;

    @InjectMocks
    private EmployeeEventController employeeEventController = new EmployeeEventController(employeeEventService);

    @InjectMocks
    private GenericExceptionHandlerAdvice genericExceptionHandlerAdvice;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);

        RestAssuredMockMvc.standaloneSetup(employeeEventController, genericExceptionHandlerAdvice);

        Mockito.when(employeeEventService.getEmployeeEventData(Mockito.anyString()))
                .thenReturn(getEmployeeEventDataResponse());
    }

    @Test
    public void testGetEmployeeDetailsSuccess() {

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + EMPLOYEE_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("", Matchers.aMapWithSize(1));
    }

    @Test
    public void testGetEmployeeDetailsWhenNoDataFound() {

        Mockito.when(employeeEventService.getEmployeeEventData(Mockito.anyString()))
                .thenThrow(new EmployeeEventNotFoundException());

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + EMPLOYEE_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("", Matchers.aMapWithSize(5));
    }

    @Test
    public void testGetEmployeeDetailsWhenUnexpectedException() {

        Mockito.when(employeeEventService.getEmployeeEventData(Mockito.anyString()))
                .thenThrow(new TakeAwayServerRuntimeException("Test Exception"));

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + EMPLOYEE_ID)
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType(ContentType.JSON)
                .body("", Matchers.aMapWithSize(5));
    }

    @Test
    public void testGetEmployeeDetailsWhenClientExceptionBadRequest() {

        Mockito.when(employeeEventService.getEmployeeEventData(Mockito.anyString()))
                .thenThrow(new TakeAwayClientRuntimeException("Test Exception"));

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + EMPLOYEE_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("", Matchers.aMapWithSize(5));
    }

    private EmployeeEventDataResponseDto getEmployeeEventDataResponse() {

        return EmployeeEventDataResponseDto.builder()
                .employeeEvents(
                        Collections.singletonList(
                                EmployeeEvent.builder()
                                        .eventType(EmployeeEventType.CREATED.name())
                                        .eventTime(ZonedDateTime.now())
                                        .build()
                        ))
                .build();
    }
}
