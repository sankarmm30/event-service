package com.takeaway.challenge.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Configuring logging request filter that writes the request URI
 * to the Commons Log.
 */
@Configuration
public class EventServiceRequestLoggingContext {

    private static final String MESSAGE_PREFIX = "REQUEST DATA : ";
    private static final int MAX_PAYLOAD_LENGTH = 10000;

    @Bean
    public CommonsRequestLoggingFilter logFilter() {

        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();

        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(MAX_PAYLOAD_LENGTH);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix(MESSAGE_PREFIX);

        return filter;
    }
}
