package com.sandemo.hrms.exception;

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 */
public class EmployeeEventNotFoundException extends GenericClientRuntimeException {

    public static final String MESSAGE = "Could not find the event for the given employeeId";

    public EmployeeEventNotFoundException() {

        super(MESSAGE);
    }
}