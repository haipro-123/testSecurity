package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;

@Data
public class LichSuGiaoDichDTO {
    @NotBlank(message = "request data is null")
    private String transactionID;
    @NotBlank(message = "request data is null")
    private String account;
    @NotBlank(message = "request data is null")
    private String inDent;
    @NotBlank(message = "request data is null")
    private String have;
    @NotBlank(message = "request data is null")
    private String time;
}
