package com.takeaway.challenge.service.impl;

import com.takeaway.challenge.dto.response.EmployeeEvent;
import com.takeaway.challenge.dto.response.EmployeeEventDataResponseDto;
import com.takeaway.challenge.exception.EmployeeEventNotFoundException;
import com.takeaway.challenge.exception.TakeAwayClientRuntimeException;
import com.takeaway.challenge.exception.TakeAwayServerRuntimeException;
import com.takeaway.challenge.model.EmployeeEventEntity;
import com.takeaway.challenge.repository.EmployeeEventEntityRepository;
import com.takeaway.challenge.service.EmployeeEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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

                throw new TakeAwayClientRuntimeException("Employee Id cannot be null or empty");
            }
        } catch(TakeAwayClientRuntimeException exception) {

            throw exception;

        } catch (Exception exception) {

            LOG.error("Exception while fetching employee event data",exception);

            throw new TakeAwayServerRuntimeException("Unexpected error occurred", exception);
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
