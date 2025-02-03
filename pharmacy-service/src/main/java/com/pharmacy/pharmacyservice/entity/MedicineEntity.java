package com.pharmacy.pharmacyservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "medicines")
public class MedicineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicine_name")
    private String medicineName;

    @Column(name = "batch_no ")
    private String batchNo ;

    @Column(name = "quantity ")
    private int quantity ;

    @Column(name = "price")
    private float price ;

    @Column(name = "created_at")
    private LocalDateTime created_at ;







}
