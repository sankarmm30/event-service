package com.takeaway.challenge.service;

import com.takeaway.challenge.model.EmployeeEventEntity;

import java.util.List;

public interface EmployeeEventService {

    /**
     * This method in charge of fetching the list of employee event for the input employeeId
     *
     * @param employeeId
     * @return
     */
    List<EmployeeEventEntity> getEventListByEmployeeId(final String employeeId);
}
