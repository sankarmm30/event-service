package com.takeaway.challenge.controller;

import com.takeaway.challenge.dto.response.EmployeeEvent;
import com.takeaway.challenge.dto.response.EmployeeEventDataResponseDto;
import com.takeaway.challenge.model.EmployeeEventEntity;
import com.takeaway.challenge.service.EmployeeEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This controller provides endpoint for managing the employee event service
 */
@RestController("employeeEventController")
@RequestMapping(value = "/employee-event")
@Api(value = "Employee event controller", description = "This controller provides endpoint to get the employee event data")
public class EmployeeEventController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeEventController.class);

    private EmployeeEventService employeeEventService;

    public EmployeeEventController(final EmployeeEventService employeeEventService) {

        this.employeeEventService = employeeEventService;
    }

    @ApiOperation(value = "Get all events related to a specific employee in ascending order",
            response = EmployeeEventDataResponseDto.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeEventDataResponseDto> getEmployeeEventDetails(
            @Valid @NotBlank(message = "Employee Id cannot be null or empty") final @RequestParam String employeeId) {

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
