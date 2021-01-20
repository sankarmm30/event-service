package com.sandemo.hrms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_event")
public class EmployeeEventEntity implements Serializable {

    private static final long serialVersionUID = -7526472295622776155L;

    @Id
    @Column(name = "ievent_id", nullable = false)
    @GeneratedValue(generator = "seq_event_id", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_event_id", sequenceName = "seq_event_id", allocationSize = 1)
    private Long eventId;

    @Column(name= "semployee_id", nullable = false, length = 50)
    private String employeeId;

    @Column(name= "sevent_type", nullable = false, length = 50)
    private String eventType;

    @Column(name= "sapp_name", nullable = false, length = 120)
    private String appName;

    @Column(name = "tsevent_time", nullable = false)
    private ZonedDateTime eventTime;

    @Column(name = "tscreated_at")
    private ZonedDateTime createdAt;
}
