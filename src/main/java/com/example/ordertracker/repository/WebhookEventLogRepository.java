package com.example.ordertracker.repository;

import com.example.ordertracker.entity.WebhookEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WebhookEventLogRepository extends JpaRepository<WebhookEventLog,Long>,
        JpaSpecificationExecutor<WebhookEventLog> {
}
