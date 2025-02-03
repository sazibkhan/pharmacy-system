package com.pharmacy.pharmacyservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
    private String batchNo;

    @Column(name = "quantity ")
    private int quantity;

    @Column(name = "price")
    private float price;

    @Column(name = "created_at")
    private LocalDateTime created_at;

}
