package com.store.store.repository;

import java.util.List;

import com.store.store.entity.dto.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.store.store.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT new com.store.store.entity.dto.ProductDto(p.productId, p.productName, p.description, p.category, p.price, p.stock) FROM ProductEntity p")
    List<ProductDto> getAll();

    @Query(value = "SELECT new com.store.store.entity.dto.ProductDto(p.productId, p.productName, p.description, p.category, p.price, p.stock) FROM ProductEntity p WHERE p.category = ?1")
    List<ProductDto> getByCategory(String category);

    @Query(value = "SELECT DISTINCT p.category FROM ProductEntity p")
    List<String> getAllCategories();

    @Query(value = "SELECT new com.store.store.entity.dto.ProductDto(p.productId, p.productName, p.description, p.category, p.price, p.stock) FROM ProductEntity p WHERE p.productName LIKE %:keyword%")
    List<ProductDto> getByProductName(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM product WHERE category = ?1 ORDER BY product_id LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<ProductEntity> findProductsPaginated(String category, Long limit, Long offset);

}