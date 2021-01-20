package com.sandemo.hrms.service.impl;

import com.sandemo.hrms.dto.response.EmployeeEvent;
import com.sandemo.hrms.dto.response.EmployeeEventDataResponseDto;
import com.sandemo.hrms.model.EmployeeEventEntity;
import com.sandemo.hrms.service.EmployeeEventService;
import com.sandemo.hrms.exception.EmployeeEventNotFoundException;
import com.sandemo.hrms.exception.GenericClientRuntimeException;
import com.sandemo.hrms.exception.GenericServerRuntimeException;
import com.sandemo.hrms.repository.EmployeeEventEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 */
@Service("employeeEventService")
public class EmployeeEventServiceImpl implements EmployeeEventService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeEventServiceImpl.class);

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

        try {

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

                throw new GenericClientRuntimeException("Employee Id cannot be null or empty");
            }
        } catch(GenericClientRuntimeException exception) {

            throw exception;

        } catch (Exception exception) {

            LOG.error("Exception while fetching employee event data",exception);

            throw new GenericServerRuntimeException("Unexpected error occurred", exception);
        }
    }

    /**
     * This method in charge of fetching the events for the specific employeeId and
     * building the Employee event data response from EmployeeEventEntity list
     *
     * @param employeeId
     * @return
     */
    @Override
    public EmployeeEventDataResponseDto getEmployeeEventData(String employeeId) {

        List<EmployeeEventEntity> employeeEventEntityList = this.getEventListByEmployeeId(employeeId);

        LOG.debug("Building EmployeeEventDataResponseDto");

        return EmployeeEventDataResponseDto.builder()
                .employeeEvents(
                        employeeEventEntityList.stream().map(
                                (EmployeeEventEntity employeeEventEntity) -> EmployeeEvent.builder()
                                        .eventType(employeeEventEntity.getEventType())
                                        .eventTime(employeeEventEntity.getEventTime())
                                        .build())
                                .collect(Collectors.toList()))
                .build();
    }
}
