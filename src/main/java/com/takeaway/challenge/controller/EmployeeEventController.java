package com.takeaway.challenge.controller;

import com.takeaway.challenge.dto.response.EmployeeEvent;
import com.takeaway.challenge.dto.response.EmployeeEventDataResponseDto;
import com.takeaway.challenge.model.EmployeeEventEntity;
import com.takeaway.challenge.service.EmployeeEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/employee-event")
public class EmployeeEventController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeEventController.class);

    private EmployeeEventService employeeEventService;

    public EmployeeEventController(final EmployeeEventService employeeEventService) {

        this.employeeEventService = employeeEventService;
    }

    @GetMapping(value = "/details", produces = "application/json", consumes = "application/json")
    public ResponseEntity<EmployeeEventDataResponseDto> getEmployeeEventDetails(final @RequestParam String employeeId) {

        List<EmployeeEventEntity> employeeEventEntityList = this.employeeEventService.getEventListByEmployeeId(employeeId);

        return ResponseEntity.ok(buildEmployeeEventDataResponse(employeeEventEntityList));
    }

    /**
     * This method is in charge of building the Employee event data response from EmployeeEventEntity list
     *
     * @param employeeEventEntityList
     * @return
     */
    private static EmployeeEventDataResponseDto buildEmployeeEventDataResponse(
            final List<EmployeeEventEntity> employeeEventEntityList) {

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
