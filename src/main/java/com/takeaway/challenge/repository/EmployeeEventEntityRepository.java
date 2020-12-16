package com.takeaway.challenge.repository;

import com.takeaway.challenge.model.EmployeeEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeEventEntityRepository extends JpaRepository<EmployeeEventEntity, Long> {

    /**
     * Fetch all events for the employee in ascending order by event time (ie. time from producer).
     *
     * @param employeeId
     * @return
     */
    List<EmployeeEventEntity> findByEmployeeIdOrderByEventTimeAsc(final String employeeId);
}
