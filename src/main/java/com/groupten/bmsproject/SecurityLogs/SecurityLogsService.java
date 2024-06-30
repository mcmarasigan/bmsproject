package com.groupten.bmsproject.SecurityLogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SecurityLogsService {

    private final SecurityLogsRepository securityLogsRepository;

    @Autowired
    public SecurityLogsService(SecurityLogsRepository securityLogsRepository) {
        this.securityLogsRepository = securityLogsRepository;
    }

    public List<SecurityLogs> getAllSecurityLogs() {
        return securityLogsRepository.findAll();
    }
}
