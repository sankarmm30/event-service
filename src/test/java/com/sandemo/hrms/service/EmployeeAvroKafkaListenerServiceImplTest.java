package com.sandemo.hrms.service;

import com.sandemo.hrms.model.EmployeeEventEntity;
import com.sandemo.hrms.repository.EmployeeEventEntityRepository;
import com.sandemo.hrms.service.impl.EmployeeAvroKafkaListenerServiceImpl;
import com.sandemo.hrms.util.Util;
import com.sandemo.hrms.EmployeeEventKey;
import com.sandemo.hrms.EmployeeEventType;
import com.sandemo.hrms.EmployeeEventValue;
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

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 *
 * This is dummy unit test in order to validate the code.
 *
 */
@RunWith(JUnit4.class)
public class EmployeeAvroKafkaListenerServiceImplTest {

    private static final String TOPIC = "test";
    private static final String EMPLOYEE_ID = "test1";
    private static final String EVENT_TYPE_CREATED = "CREATED";
    private static final String EVENT_TYPE_UPDATED = "UPDATED";
    private static final String APP_NAME = "testapp";
    private static final Long EVENT_CREATED_ID = 1L;

    @Mock
    private EmployeeEventEntityRepository employeeEventEntityRepository;

    @InjectMocks
    private EmployeeAvroKafkaListenerServiceImpl employeeAvroKafkaListenerService =
            new EmployeeAvroKafkaListenerServiceImpl(employeeEventEntityRepository);

    @Mock
    private EmployeeEventEntity employeeEventEntityMock;
    @Mock
    private EmployeeEventValue employeeEventValueMock;
    @Mock
    private EmployeeEventKey employeeEventKeyMock;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);

        Mockito.when(employeeEventKeyMock.getEmployeeId()).thenReturn(EMPLOYEE_ID);
        Mockito.when(employeeEventValueMock.getType()).thenReturn(EmployeeEventType.CREATED);
        Mockito.when(employeeEventValueMock.getAppName()).thenReturn(APP_NAME);
        Mockito.when(employeeEventValueMock.getTime()).thenReturn(
                Util.getFormattedTimestamp(ZonedDateTime.now()));

        Mockito.when(employeeEventEntityRepository.save(Mockito.any(EmployeeEventEntity.class)))
                .thenReturn(getEmployeeEventEntity());
    }

    @Test
    public void testConsumeSuccess() {

        employeeAvroKafkaListenerService.consume(employeeEventValueMock, employeeEventKeyMock, TOPIC, "0", "0");

        Assert.assertTrue("Always True", true);
    }

    @Test
    public void testConsumeWhenUnexpectedException() {

        Mockito.when(employeeEventEntityRepository.save(Mockito.any(EmployeeEventEntity.class)))
                .thenThrow(new IllegalStateException("Test Exception"));

        employeeAvroKafkaListenerService.consume(employeeEventValueMock, employeeEventKeyMock, TOPIC, "0", "0");

        Assert.assertTrue("Always True", true);
    }

    private EmployeeEventEntity getEmployeeEventEntity() {

        return EmployeeEventEntity.builder()
                .eventId(EVENT_CREATED_ID)
                .employeeId(EMPLOYEE_ID).build();
    }
}
