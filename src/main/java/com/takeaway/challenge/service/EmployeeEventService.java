package com.takeaway.challenge.service;

import com.takeaway.challenge.dto.response.EmployeeEventDataResponseDto;
import com.takeaway.challenge.model.EmployeeEventEntity;

import java.util.List;

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
