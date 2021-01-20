package com.sandemo.hrms.repository;

import com.sandemo.hrms.model.EmployeeEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sankar M <sankar.mm30@gmail.com>
 */
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
