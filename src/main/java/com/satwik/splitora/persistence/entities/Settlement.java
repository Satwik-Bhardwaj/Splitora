//package com.satwik.splitwiseclone.persistence.models;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Data
//@Entity
//@Table(name = "Settlement")
//public class Settlement {
//
//    @Id
//    @Column(name = "settlement_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "payer_id")
//    private int payerId;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "payee_id")
//    private int payeeId;
//
//    @Column(name = "amount")
//    private double amount;
//
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "date")
//    private String date;
//
//    @Column(name = "created_at")
//    private String createAt;
//
//}
