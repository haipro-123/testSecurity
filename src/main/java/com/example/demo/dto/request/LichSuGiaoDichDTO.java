package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;

@Data
@AllArgsConstructor
@ToString
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
