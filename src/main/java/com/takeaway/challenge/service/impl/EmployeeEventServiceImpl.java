package com.takeaway.challenge.service.impl;

import com.takeaway.challenge.exception.EmployeeEventNotFoundException;
import com.takeaway.challenge.exception.TakeAwayClientRuntimeException;
import com.takeaway.challenge.model.EmployeeEventEntity;
import com.takeaway.challenge.repository.EmployeeEventEntityRepository;
import com.takeaway.challenge.service.EmployeeEventService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("employeeEventService")
public class EmployeeEventServiceImpl implements EmployeeEventService {

    private EmployeeEventEntityRepository employeeEventEntityRepository;

    public EmployeeEventServiceImpl(final EmployeeEventEntityRepository employeeEventEntityRepository) {

        this.employeeEventEntityRepository = employeeEventEntityRepository;
    }

    /**
     * This method in charge of fetching the employee events for the given employeeId
     *
     * @param employeeId
     * @return
     */
    @Override
    public List<EmployeeEventEntity> getEventListByEmployeeId(String employeeId) {

        if(StringUtils.hasText(employeeId)) {

            List<EmployeeEventEntity> employeeEventEntityList =
                    this.employeeEventEntityRepository.findByEmployeeIdOrderByEventTimeAsc(employeeId);

            // Return the list of data if data exists for the given employeeId
            // else throw EmployeeEventNotFoundException

            if (!employeeEventEntityList.isEmpty()) {

                return employeeEventEntityList;
            }

            throw new EmployeeEventNotFoundException();
        }else {

            throw new TakeAwayClientRuntimeException("Employee Id cannot be null or empty");
        }
    }
}
