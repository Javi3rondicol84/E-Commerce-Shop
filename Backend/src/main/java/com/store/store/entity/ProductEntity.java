package com.store.store.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class ProductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long productId;
    private String productName;
    private String description;
    private String category;
    private Double price;
    private int stock;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;

}
