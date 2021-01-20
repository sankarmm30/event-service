package com.sandemo.hrms.controller;

import com.sandemo.hrms.dto.response.EmployeeEvent;
import com.sandemo.hrms.dto.response.EmployeeEventDataResponseDto;
import com.sandemo.hrms.exception.EmployeeEventNotFoundException;
import com.sandemo.hrms.exception.GenericClientRuntimeException;
import com.sandemo.hrms.exception.GenericServerRuntimeException;
import com.sandemo.hrms.exception.advice.GenericExceptionHandlerAdvice;
import com.sandemo.hrms.service.EmployeeEventService;
import com.sandemo.hrms.EmployeeEventType;
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

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 */
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
                .thenThrow(new GenericServerRuntimeException("Test Exception"));

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
                .thenThrow(new GenericClientRuntimeException("Test Exception"));

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
