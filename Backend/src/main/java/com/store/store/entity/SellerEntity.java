package com.store.store.entity;

import com.store.store.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "seller")
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;
    @OneToMany(mappedBy = "seller", cascade = CascadeType.PERSIST)
    private List<ProductEntity> products;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SellerEntity(User user) {
        this.products = new ArrayList<ProductEntity>();
        this.user = user;
    }

    public SellerEntity(List<ProductEntity> products, User user) {
        this.products = new ArrayList<>();
        this.user = user;
    }


    public SellerEntity() {

    }
}
