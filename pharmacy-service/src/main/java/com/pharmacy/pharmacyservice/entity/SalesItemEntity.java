package com.pharmacy.pharmacyservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sales_items")
public class SalesItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id", foreignKey = @ForeignKey(name = "sales_items_sales_id_fk"))
    private SalesEntity sales;

    @Column(name = "sales_id", insertable = false, updatable = false)
    private Long salesId;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "purchase_price")
    private Double purchasePrice;

    @Column(name = "sales_price")
    private Double salesPrice;

    @Column(name = "discount_amount")
    private Double discountAmount;

}
