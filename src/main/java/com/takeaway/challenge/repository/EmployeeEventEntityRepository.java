package com.takeaway.challenge.repository;

import com.takeaway.challenge.model.EmployeeEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeEventEntityRepository extends JpaRepository<EmployeeEventEntity, Long> {

    List<EmployeeEventEntity> findByEmployeeId(final String employeeId);
}
