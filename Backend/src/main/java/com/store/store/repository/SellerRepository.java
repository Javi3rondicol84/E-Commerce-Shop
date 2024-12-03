package com.store.store.repository;

import com.store.store.entity.SellerEntity;
import com.store.store.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SellerRepository extends JpaRepository<SellerEntity, Long> {

    @Query(value = "SELECT s FROM SellerEntity s WHERE s.user= :user")
    SellerEntity getSellerByUser(@Param("user") User user);

}
