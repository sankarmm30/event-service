CREATE SCHEMA event AUTHORIZATION postgres01;

-- SEQUENCE: event.seq_event_id

-- DROP SEQUENCE event.seq_event_id;

CREATE SEQUENCE event.seq_event_id
    INCREMENT 1
    START 1
    MINVALUE 0
    MAXVALUE 9999999999999
    CACHE 1;

ALTER SEQUENCE event.seq_event_id OWNER to postgres01;

-- Table: event.employee_event

-- DROP TABLE event.employee_event;

CREATE TABLE event.employee_event
(
    ievent_id bigint NOT NULL,
    semployee_id character varying(50) NOT NULL,
    sevent_type character varying(50) NOT NULL,
    sapp_name character varying(120) NOT NULL,
    tsevent_time timestamp with time zone NOT NULL,
    tscreated_at timestamp with time zone,
    CONSTRAINT pk_event_id PRIMARY KEY (ievent_id)
);

ALTER TABLE event.employee_event OWNER to postgres01;