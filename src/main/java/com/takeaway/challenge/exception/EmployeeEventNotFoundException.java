package com.takeaway.challenge.exception;

public class EmployeeEventNotFoundException extends TakeAwayClientRuntimeException {

    public static final String MESSAGE = "Could not find the event for the given employeeId";

    public EmployeeEventNotFoundException() {

        super(MESSAGE);
    }
}