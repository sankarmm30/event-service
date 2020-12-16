package com.takeaway.challenge.service.impl;

import com.takeaway.challenge.EmployeeEventKey;
import com.takeaway.challenge.EmployeeEventValue;
import com.takeaway.challenge.model.EmployeeEventEntity;
import com.takeaway.challenge.repository.EmployeeEventEntityRepository;
import com.takeaway.challenge.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * This service is in charge of listening to kafka event and load the event data into database table.
 *
 */
@Service("employeeAvroKafkaListenerService")
public class EmployeeAvroKafkaListenerServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeAvroKafkaListenerServiceImpl.class);

    private EmployeeEventEntityRepository employeeEventEntityRepository;

    public EmployeeAvroKafkaListenerServiceImpl(final EmployeeEventEntityRepository employeeEventEntityRepository) {

        this.employeeEventEntityRepository = employeeEventEntityRepository;
    }

    @KafkaListener(topics = "${kafka.consumer.employee.topic}", containerGroup = "${kafka.consumer.group.id}")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void consume(@Payload EmployeeEventValue employeeEventValue,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) EmployeeEventKey employeeEventKey,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String partition,
                        @Header(KafkaHeaders.OFFSET) String offset) {

        LOG.info("Message: '{}' from TOPIC '{}', PARTITION '{}', OFFSET {} has been received",
                employeeEventValue, topic, partition, offset);

        try{

            EmployeeEventEntity employeeEventEntity = this.employeeEventEntityRepository.save(
                    EmployeeEventEntity.builder()
                            .employeeId(Util.charSequenceToString(employeeEventKey.getEmployeeId()))
                            .eventType(employeeEventValue.getType().name())
                            .appName(Util.charSequenceToString(employeeEventValue.getAppName()))
                            .eventTime(Util.getZonedDateTime(
                                    Util.charSequenceToString(employeeEventValue.getTime())))
                            .createdAt(ZonedDateTime.now())
                            .build());

            LOG.info("Employee event record has been created with id: {}", employeeEventEntity.getEventId());

        } catch (Exception exception) {

            LOG.warn("Exception while processing the message", exception);
        }
    }
}
