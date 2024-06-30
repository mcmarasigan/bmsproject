package com.groupten.bmsproject.SecurityLogs;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class SecurityLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String activityLog;
    private String timestamp;

    // Constructors
    public SecurityLogs() {}

    public SecurityLogs(String username, String activityLog, LocalDateTime timestamp) {
        this.username = username;
        this.activityLog = activityLog;
        this.timestamp = formatTimestamp(timestamp);
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActivityLog() {
        return activityLog;
    }

    public void setActivityLog(String activityLog) {
        this.activityLog = activityLog;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = formatTimestamp(timestamp);
    }

    private String formatTimestamp(LocalDateTime timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        return timestamp.format(formatter);
    }

    @Override
    public String toString() {
        return "SecurityLogs{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", activityLog='" + activityLog + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
