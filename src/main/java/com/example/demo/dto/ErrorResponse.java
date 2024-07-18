package com.example.demo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorResponse {
    private Date timeStamp;
    private String path;
    private String message;
}
