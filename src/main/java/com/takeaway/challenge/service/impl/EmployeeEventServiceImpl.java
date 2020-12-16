package com.takeaway.challenge.service.impl;

import com.takeaway.challenge.exception.EmployeeEventNotFoundException;
import com.takeaway.challenge.model.EmployeeEventEntity;
import com.takeaway.challenge.repository.EmployeeEventEntityRepository;
import com.takeaway.challenge.service.EmployeeEventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("employeeEventService")
public class EmployeeEventServiceImpl implements EmployeeEventService {

    private EmployeeEventEntityRepository employeeEventEntityRepository;

    public EmployeeEventServiceImpl(final EmployeeEventEntityRepository employeeEventEntityRepository) {

        this.employeeEventEntityRepository = employeeEventEntityRepository;
    }

    @Override
    public List<EmployeeEventEntity> getEventListByEmployeeId(String employeeId) {

        List<EmployeeEventEntity> employeeEventEntityList =
                this.employeeEventEntityRepository.findByEmployeeIdOrderByEventTimeAsc(employeeId);

        if (!employeeEventEntityList.isEmpty()) {

            return employeeEventEntityList;
        }

        throw new EmployeeEventNotFoundException();
    }
}
