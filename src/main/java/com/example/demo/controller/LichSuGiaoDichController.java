package com.example.demo.controller;

import com.example.demo.dto.LichSuGiaoDichDTO;
import com.example.demo.service.LichSuGiaoDichService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

@RestController
public class LichSuGiaoDichController {
    @Autowired
    private LichSuGiaoDichService lichSuGiaoDichService;
    @PostMapping("")
    public ResponseEntity<?> add(@Valid @NotNull @RequestBody LichSuGiaoDichDTO lichSuGiaoDichDTO) throws BadPaddingException, ParseException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        lichSuGiaoDichService.add(lichSuGiaoDichDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
