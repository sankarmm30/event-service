package com.takeaway.challenge.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Validation configuration for validating the bean in programmatically
 */
@Configuration
public class ValidationFactoryContext {

    @Bean("validator")
    public Validator validator() {

        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
