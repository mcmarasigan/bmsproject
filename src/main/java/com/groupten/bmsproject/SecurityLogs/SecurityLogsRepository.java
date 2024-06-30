package com.groupten.bmsproject.SecurityLogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityLogsRepository extends JpaRepository<SecurityLogs, Long> {
}
