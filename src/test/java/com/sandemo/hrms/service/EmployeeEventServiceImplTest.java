package com.sandemo.hrms.service;

import com.sandemo.hrms.dto.response.EmployeeEventDataResponseDto;
import com.sandemo.hrms.exception.EmployeeEventNotFoundException;
import com.sandemo.hrms.exception.GenericClientRuntimeException;
import com.sandemo.hrms.exception.GenericServerRuntimeException;
import com.sandemo.hrms.model.EmployeeEventEntity;
import com.sandemo.hrms.repository.EmployeeEventEntityRepository;
import com.sandemo.hrms.service.impl.EmployeeEventServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 */
@RunWith(JUnit4.class)
public class EmployeeEventServiceImplTest {

    private static final String EMPLOYEE_ID = "test1";
    private static final String EVENT_TYPE_CREATED = "CREATED";
    private static final String EVENT_TYPE_UPDATED = "UPDATED";
    private static final String APP_NAME = "testapp";
    private static final Long EVENT_CREATED_ID = 1L;
    private static final Long EVENT_UPDATED_ID = 1L;

    @Mock
    private EmployeeEventEntityRepository employeeEventEntityRepository;

    @InjectMocks
    private EmployeeEventService employeeEventService = new EmployeeEventServiceImpl(employeeEventEntityRepository);

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);

        Mockito.when(this.employeeEventEntityRepository.findByEmployeeIdOrderByEventTimeAsc(Mockito.eq(EMPLOYEE_ID)))
                .thenReturn(getEmployeeEventEntityList());
    }

    @Test
    public void testGetEventListByEmployeeIdValid() {

        List<EmployeeEventEntity> employeeEventEntityList =
                this.employeeEventService.getEventListByEmployeeId(EMPLOYEE_ID);

        Assert.assertFalse(employeeEventEntityList.isEmpty());
        Assert.assertEquals(2, employeeEventEntityList.size());
        Assert.assertEquals(EVENT_TYPE_CREATED, employeeEventEntityList.get(0).getEventType());
        Assert.assertEquals(EVENT_TYPE_UPDATED, employeeEventEntityList.get(1).getEventType());
    }

    @Test(expected = EmployeeEventNotFoundException.class)
    public void testGetEventListByEmployeeIdWhenNoDataFound() {

        // when
        Mockito.when(this.employeeEventEntityRepository.findByEmployeeIdOrderByEventTimeAsc(Mockito.eq(EMPLOYEE_ID)))
                .thenReturn(Collections.emptyList());

        this.employeeEventService.getEventListByEmployeeId(EMPLOYEE_ID);
    }

    @Test(expected = GenericClientRuntimeException.class)
    public void testGetEventListByEmployeeIdWhenEmployeeIdIsNull() {

        // When employeeId is null
        this.employeeEventService.getEventListByEmployeeId(null);
    }

    @Test(expected = GenericClientRuntimeException.class)
    public void testGetEventListByEmployeeIdWhenEmployeeIdIsEmpty() {

        // When employeeId is null
        this.employeeEventService.getEventListByEmployeeId("");
    }

    @Test(expected = GenericClientRuntimeException.class)
    public void testGetEventListByEmployeeIdWhenEmployeeIdContainsWhiteSpace() {

        // When employeeId is null
        this.employeeEventService.getEventListByEmployeeId("  ");
    }

    @Test(expected = GenericServerRuntimeException.class)
    public void testGetEventListByEmployeeIdWhenUnexpectedException() {

        // when
        Mockito.when(this.employeeEventEntityRepository.findByEmployeeIdOrderByEventTimeAsc(Mockito.eq(EMPLOYEE_ID)))
                .thenThrow(new IllegalStateException());

        this.employeeEventService.getEventListByEmployeeId(EMPLOYEE_ID);
    }

    @Test
    public void testGetEmployeeEventDataValid() {

        EmployeeEventDataResponseDto employeeEventDataResponseDto = this.employeeEventService.getEmployeeEventData(EMPLOYEE_ID);

        Assert.assertNotNull(employeeEventDataResponseDto);
        Assert.assertFalse(employeeEventDataResponseDto.getEmployeeEvents().isEmpty());
        Assert.assertEquals(2, employeeEventDataResponseDto.getEmployeeEvents().size());
        Assert.assertEquals(EVENT_TYPE_CREATED, employeeEventDataResponseDto.getEmployeeEvents().get(0).getEventType());
        Assert.assertEquals(EVENT_TYPE_UPDATED, employeeEventDataResponseDto.getEmployeeEvents().get(1).getEventType());
    }

    private List<EmployeeEventEntity> getEmployeeEventEntityList() {

        List<EmployeeEventEntity> employeeEventEntityList = new ArrayList<>();

        employeeEventEntityList.add(
                EmployeeEventEntity.builder()
                        .eventId(EVENT_CREATED_ID)
                        .employeeId(EMPLOYEE_ID)
                        .eventType(EVENT_TYPE_CREATED)
                        .appName(APP_NAME)
                        .eventTime(ZonedDateTime.now())
                        .createdAt(ZonedDateTime.now())
                        .build());

        employeeEventEntityList.add(
                EmployeeEventEntity.builder()
                        .eventId(EVENT_UPDATED_ID)
                        .employeeId(EMPLOYEE_ID)
                        .eventType(EVENT_TYPE_UPDATED)
                        .appName(APP_NAME)
                        .eventTime(ZonedDateTime.now())
                        .createdAt(ZonedDateTime.now())
                        .build());

        return employeeEventEntityList;
    }
}
