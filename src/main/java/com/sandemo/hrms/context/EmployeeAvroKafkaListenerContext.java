package com.sandemo.hrms.context;

import com.sandemo.hrms.EmployeeEventKey;
import com.sandemo.hrms.EmployeeEventValue;
import com.sandemo.hrms.exception.GenericRuntimeException;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.subject.TopicRecordNameStrategy;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 *
 * Configuring the kafka listener.
 */
@EnableKafka
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class EmployeeAvroKafkaListenerContext {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeAvroKafkaListenerContext.class);

    private static final String KAFKA_CONSUMER_PROP_PREFIX = "kafka.consumer.";
    private static final String KAFKA_CONSUMER_ERROR_RETRY_PROP = "kafka.consumer.error.retry.attempts";
    private static final String KAFKA_CONSUMER_ERROR_RETRY_DELAY_PROP = "kafka.consumer.error.retry.delay";
    private static final String DEFAULT_SCHEMA_REGISTRY_URL = "http://localhost:8081";
    private static final Boolean SPECIFIC_AVRO_READER_CONFIG_VALUE = true;
    private static final Boolean AUTO_REGISTER_SCHEMAS = false;
    private static final int DEFAULT_RETRY_MAX = 3;
    private static final int DEFAULT_RETRY_DELAY = 10000;
    private static final int IDENTITY_MAP_CAPACITY = 100;

    @Autowired
    private Environment environment;

    public EmployeeAvroKafkaListenerContext(Environment environment) {

        this.environment = environment;
    }

    /**
     * Schema registry properties
     *
     * @return
     */
    @Bean
    public Map<String, Object> schemaRegistryProps(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();

        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
                environment.getProperty(KAFKA_CONSUMER_PROP_PREFIX + KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
                        DEFAULT_SCHEMA_REGISTRY_URL));
        props.put(KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS,
                environment.getProperty(KAFKA_CONSUMER_PROP_PREFIX + KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS,
                        Boolean.class, AUTO_REGISTER_SCHEMAS));
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG,
                environment.getProperty(KAFKA_CONSUMER_PROP_PREFIX + KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG,
                        Boolean.class, SPECIFIC_AVRO_READER_CONFIG_VALUE));
        props.put(KafkaAvroDeserializerConfig.KEY_SUBJECT_NAME_STRATEGY,
                environment.getProperty(KAFKA_CONSUMER_PROP_PREFIX + KafkaAvroDeserializerConfig.KEY_SUBJECT_NAME_STRATEGY,
                        TopicRecordNameStrategy.class.getName()));
        props.put(KafkaAvroDeserializerConfig.VALUE_SUBJECT_NAME_STRATEGY,
                environment.getProperty(KAFKA_CONSUMER_PROP_PREFIX + KafkaAvroDeserializerConfig.VALUE_SUBJECT_NAME_STRATEGY,
                        TopicRecordNameStrategy.class.getName()));

        return props;
    }

    /**
     * Schema registry client configuration for client side caching
     *
     * @return
     */
    @Bean
    public SchemaRegistryClient schemaRegistryClient() {

        return new CachedSchemaRegistryClient(
                environment.getProperty(KAFKA_CONSUMER_PROP_PREFIX + KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
                        DEFAULT_SCHEMA_REGISTRY_URL),
                IDENTITY_MAP_CAPACITY);
    }

    /**
     * Consumer factory configuration for avro key and value type
     *
     * @param kafkaProperties
     * @return
     */
    @Bean
    public ConsumerFactory<EmployeeEventKey, EmployeeEventValue> consumerFactory(KafkaProperties kafkaProperties) {

        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(),
                (Deserializer) new KafkaAvroDeserializer(schemaRegistryClient(), schemaRegistryProps(kafkaProperties)),
                (Deserializer) new KafkaAvroDeserializer(schemaRegistryClient(), schemaRegistryProps(kafkaProperties)));
    }

    /**
     * Kafka container listener configuration
     *
     * @param kafkaProperties
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<EmployeeEventKey, EmployeeEventValue>> kafkaListenerContainerFactory(KafkaProperties kafkaProperties) {

        ConcurrentKafkaListenerContainerFactory<EmployeeEventKey, EmployeeEventValue> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory(kafkaProperties));
        factory.setAckDiscarded(true);
        factory.setErrorHandler(errorHandler());                // Setting up error handler

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);

        return factory;
    }

    /**
     * Kafka listener error handler
     *
     * @return
     */
    @Bean
    public SeekToCurrentErrorHandler errorHandler() {

        SeekToCurrentErrorHandler handler =
                new SeekToCurrentErrorHandler(
                        new FixedBackOff(
                                environment.getProperty(KAFKA_CONSUMER_ERROR_RETRY_DELAY_PROP, Integer.class, DEFAULT_RETRY_DELAY),
                                environment.getProperty(KAFKA_CONSUMER_ERROR_RETRY_PROP, Integer.class, DEFAULT_RETRY_MAX)));

        // Adding exception for not to retry
        handler.addNotRetryableException(GenericRuntimeException.class);
        handler.addNotRetryableException(ConstraintViolationException.class);

        return handler;
    }
}
