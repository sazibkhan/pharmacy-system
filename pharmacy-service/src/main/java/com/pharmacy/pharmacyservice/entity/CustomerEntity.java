package com.pharmacy.pharmacyservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "customer_type")
    private String customerType;

    @Column(name = "email")
    private String email;

    @Column(name = "customer_image")
    private String customerImage;

    @Column(name = "enabled")
    private Boolean enabled;


}
