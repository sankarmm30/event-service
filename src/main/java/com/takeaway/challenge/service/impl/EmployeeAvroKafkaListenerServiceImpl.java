package com.takeaway.challenge.service.impl;

import com.takeaway.challenge.EmployeeEventKey;
import com.takeaway.challenge.EmployeeEventValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * This service is in charge of listening to kafka event and load the event data into database table.
 *
 */
@Service
public class EmployeeAvroKafkaListenerServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeAvroKafkaListenerServiceImpl.class);

    @KafkaListener(topics = "${kafka.consumer.employee.topic}", containerGroup = "${kafka.consumer.group.id}")
    public void consume(@Payload EmployeeEventValue employeeEventValue,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY)EmployeeEventKey employeeEventKey,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String partition,
                        @Header(KafkaHeaders.OFFSET) String offset) {

        LOG.info("Message: '{}' from TOPIC '{}', PARTITION '{}', OFFSET {} has been received",
                employeeEventValue, topic, partition, offset);
    }
}
