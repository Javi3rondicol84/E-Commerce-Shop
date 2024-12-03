package com.store.store.service.impl;

import com.store.store.entity.ProductEntity;
import com.store.store.entity.dto.ProductDto;
import com.store.store.helper.GenericHttpHelper;
import com.store.store.helper.ProductHttpHelper;
import com.store.store.repository.ProductRepository;
import com.store.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductHttpHelper onlyProductHttpHelper;

    @Autowired
    private GenericHttpHelper genericHttpHelper;

    @Autowired
    private GenericHttpHelper<ProductEntity> productHttpHelper;

    @Override
    public ResponseEntity<?> getAllProducts() {
        List<ProductDto> products = this.productRepository.getAll();

        return this.genericHttpHelper.getAllItemsResponse(products, "productos");
    }

    @Override
    public ResponseEntity<?> getProductById(Long id) {
        Optional<ProductEntity> product = this.productRepository.findById(id);

        ProductEntity p = product.get();

        ProductDto productDto = new ProductDto(p.getProductId(), p.getProductName(), p.getDescription(), p.getCategory(), p.getPrice(), p.getStock());

        return this.genericHttpHelper.getItemByIdResponse(productDto, id, "producto");
    }

    @Override
    public ResponseEntity<?> createProduct(ProductEntity product) {
        if (product != null) {
            this.productRepository.save(product);
        }

        ProductDto productDto = new ProductDto(product.getProductId(), product.getProductName(), product.getDescription(), product.getCategory(), product.getPrice(), product.getStock());

        return this.genericHttpHelper.getPostResponse(productDto, "producto");
    }

    @Override
    public ResponseEntity<?> updateProduct(ProductEntity newProduct, Long id) {
        Optional<ProductEntity> oldProduct = this.productRepository.findById(id);

        ProductEntity product = oldProduct.get();

        if (oldProduct.isPresent()) {
            product.setProductName(newProduct.getProductName());
            product.setDescription(newProduct.getDescription());
            product.setCategory(newProduct.getCategory());
            product.setPrice(newProduct.getPrice());
            product.setStock(newProduct.getStock());

            this.productRepository.save(product);
        }

        ProductDto productDto = new ProductDto(product.getProductId(), product.getProductName(), product.getDescription(), product.getCategory(), product.getPrice(), product.getStock());

        return this.genericHttpHelper.getPutResponse(productDto, id, "producto");
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        Optional<ProductEntity> product = this.productRepository.findById(id);

        if (product.isPresent()) {
            this.productRepository.delete(product.get());
        }

        ProductDto productDto = new ProductDto(product.get().getProductId(), product.get().getProductName(), product.get().getDescription(), product.get().getCategory(), product.get().getPrice(), product.get().getStock());

        return this.genericHttpHelper.getDeleteResponse(productDto, id, "producto");
    }

    @Override
    public ResponseEntity<?> getByCategory(String category) {  //SE USA
        List<ProductDto> products = this.productRepository.getByCategory(category);

        return this.onlyProductHttpHelper.getByCategoryResponse(products, category);
    }


    @Override
    public ResponseEntity<?> getAllCategories() {
        List<String> products = this.productRepository.getAllCategories();

        return this.onlyProductHttpHelper.getAllCategoriesResponse(products);
    }

    @Override
    public ResponseEntity<?> getProductByProductName(String keyword) {
        List<ProductDto> products = this.productRepository.getByProductName(keyword);
        return this.onlyProductHttpHelper.getProductsByProductNameResponse(products, keyword);
    }

    @Override
    public ResponseEntity<?> getProductsPaginated(String category, Long limit, Long offset) {
        List<ProductEntity> products = this.productRepository.findProductsPaginated(category, limit, offset);

        return this.onlyProductHttpHelper.getProductsPaginatedResponse(products, category, limit, offset);
    }
}
