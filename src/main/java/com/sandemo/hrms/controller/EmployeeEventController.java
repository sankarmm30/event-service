package com.sandemo.hrms.controller;

import com.sandemo.hrms.dto.response.EmployeeEventDataResponseDto;
import com.sandemo.hrms.service.EmployeeEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 *
 * This controller provides endpoint for managing the employee event service
 */
@RestController("employeeEventController")
@RequestMapping(value = "/employee-event")
@Api(value = "Employee event controller", description = "This controller provides endpoint to get the employee event data")
public class EmployeeEventController {

    private EmployeeEventService employeeEventService;

    public EmployeeEventController(final EmployeeEventService employeeEventService) {

        this.employeeEventService = employeeEventService;
    }

    @ApiOperation(value = "Get all events related to a specific employee in ascending order",
            response = EmployeeEventDataResponseDto.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeEventDataResponseDto> getEmployeeEventDetails(
            @Valid @NotBlank(message = "Employee Id cannot be null or empty") final @RequestParam String employeeId) {

        return ResponseEntity.ok(this.employeeEventService.getEmployeeEventData(employeeId));
    }
}
