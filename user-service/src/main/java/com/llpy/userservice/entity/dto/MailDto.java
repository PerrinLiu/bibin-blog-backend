package com.llpy.userservice.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MailDto {
    private String id;
    private String from;
    private String to;
    private String subject;
    private String text;
    private LocalDateTime sentDate;
    private String status;
    private String error;
}