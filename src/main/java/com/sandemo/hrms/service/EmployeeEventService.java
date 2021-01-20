package com.sandemo.hrms.service;

import com.sandemo.hrms.dto.response.EmployeeEventDataResponseDto;
import com.sandemo.hrms.model.EmployeeEventEntity;

import java.util.List;

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 */
public interface EmployeeEventService {

    /**
     * This method in charge of fetching the events for the specific employeeId
     *
     * @param employeeId
     * @return
     */
    List<EmployeeEventEntity> getEventListByEmployeeId(final String employeeId);

    /**
     * This method in charge of fetching the events for the specific employeeId and
     * building the Employee event data response from EmployeeEventEntity list
     *
     * @param employeeId
     * @return
     */
    EmployeeEventDataResponseDto getEmployeeEventData(final String employeeId);
}
