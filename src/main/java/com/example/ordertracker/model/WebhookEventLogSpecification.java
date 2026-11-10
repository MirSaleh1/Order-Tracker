package com.example.ordertracker.model;

import com.example.ordertracker.entity.WebhookEventLog;
import com.example.ordertracker.model.request.WebhookLogFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public interface WebhookEventLogSpecification {

    static Specification<WebhookEventLog> toSpecification(WebhookLogFilterRequest filterRequest) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterRequest.provider() != null && !filterRequest.provider().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("provider")), "%" + filterRequest.provider().toLowerCase() + "%"));
            }

            if (filterRequest.status() != null) {
                predicates.add(cb.equal(root.get("status"), filterRequest.status()));
            }

            if (filterRequest.startDate() != null && filterRequest.endDate() != null) {
                predicates.add(cb.between(
                        root.get("receivedAt"),
                        filterRequest.startDate().atStartOfDay(),
                        filterRequest.endDate().atTime(LocalTime.MAX)
                ));
            } else if (filterRequest.startDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("receivedAt"), filterRequest.startDate().atStartOfDay()));
            } else if (filterRequest.endDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("receivedAt"), filterRequest.endDate().atTime(LocalTime.MAX)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
