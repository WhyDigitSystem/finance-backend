package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "email_notification_config")
@Data
public class EmailNotificationConfigVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String moduleCode;

    @Column(columnDefinition = "TEXT")
    private String toEmails;

    @Column(columnDefinition = "TEXT")
    private String ccEmails;

    private String isActive;
}
