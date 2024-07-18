package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "lichSUGiaoDich")
@Getter @NoArgsConstructor @AllArgsConstructor
public class LichSuGiaoDich {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "transactionID")
    private String transactionID;
    private String account;
    @Column(name = "inDent")
    private Double inDent;
    private Integer have;
    private Date time;

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setInDent(Double inDent) {
        this.inDent = inDent;
    }

    public void setHave(Integer have) {
        this.have = have;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
