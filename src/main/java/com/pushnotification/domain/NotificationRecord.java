package com.pushnotification.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "tbl_notification_logs")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NotificationRecord {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;

    @Column(name = "message_type", nullable = false, length = 50)
    private String messageType;

    @Column(nullable = false, length = 100)
    private String channels;

    @Column(length = 255)
    private String email;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(length = 30)
    private String phone;

    @Column(length = 200)
    private String subject;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "template_data", columnDefinition = "jsonb")
    private Map<String, Object> templateData;

    @Column(name = "kvkk_approved", nullable = false)
    private boolean kvkkApproved;

    @Column(name = "kvkk_approved_at")
    private OffsetDateTime kvkkApprovedAt;

    @Column(name = "read", nullable = false)
    private boolean read;

    @Column(name = "read_at")
    private OffsetDateTime readAt;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 512)
    private String userAgent;

    @Column(name = "device_info", length = 255)
    private String deviceInfo;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
