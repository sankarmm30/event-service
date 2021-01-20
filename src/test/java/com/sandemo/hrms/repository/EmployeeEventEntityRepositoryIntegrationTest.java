package com.sandemo.hrms.repository;

import com.sandemo.hrms.model.EmployeeEventEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 */
@RunWith(SpringRunner.class)
@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class)
@PropertySource("classpath:application-test.properties")
public class EmployeeEventEntityRepositoryIntegrationTest {

    private static final String EMPLOYEE_ID = "test1";
    private static final String INVALID_EMPLOYEE_ID = "invalidtest1";
    private static final String EVENT_TYPE_CREATED = "CREATED";
    private static final String EVENT_TYPE_UPDATED = "UPDATED";
    private static final String APP_NAME = "testapp";

    @Autowired
    private EmployeeEventEntityRepository employeeEventEntityRepository;

    @Before
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
    public void testFindAll() {

        List<EmployeeEventEntity> employeeEventEntityList = this.employeeEventEntityRepository.findAll();

        Assert.assertFalse(employeeEventEntityList.isEmpty());
        Assert.assertEquals(2, employeeEventEntityList.size());
    }

    @Test
    public void testFindByEmployeeIdOrderByEventTimeAscValid() {

        List<EmployeeEventEntity> employeeEventEntityList = this.employeeEventEntityRepository
                .findByEmployeeIdOrderByEventTimeAsc(EMPLOYEE_ID);

        Assert.assertFalse(employeeEventEntityList.isEmpty());
        Assert.assertEquals(2, employeeEventEntityList.size());
        Assert.assertEquals(EVENT_TYPE_CREATED, employeeEventEntityList.get(0).getEventType());
        Assert.assertEquals(EVENT_TYPE_UPDATED, employeeEventEntityList.get(1).getEventType());
    }

    @Test
    public void testFindByEmployeeIdOrderByEventTimeAscInvalidTest() {

        List<EmployeeEventEntity> employeeEventEntityList = this.employeeEventEntityRepository
                .findByEmployeeIdOrderByEventTimeAsc(INVALID_EMPLOYEE_ID);

        Assert.assertTrue(employeeEventEntityList.isEmpty());
    }
}
