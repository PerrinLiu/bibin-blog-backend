package com.llpy.userservice.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 邮寄至
 *
 * @author LLPY
 * @date 2023/11/08
 */
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