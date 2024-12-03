package com.store.store.service.impl;

import com.store.store.entity.CartEntity;
import com.store.store.entity.dto.CartDto;
import com.store.store.entity.dto.ProductCartDto;
import com.store.store.entity.ProductEntity;
import com.store.store.helper.GenericHttpHelper;
import com.store.store.repository.CartRepository;
import com.store.store.repository.ProductRepository;
import com.store.store.service.CartService;
import com.store.store.user.User;
import com.store.store.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenericHttpHelper<CartDto> cartGenericHttpHelper;

    @Override
    public ResponseEntity<?> getAllCarts() {
        //traer lista de CartDto
        List<CartDto> carts = this.cartRepository.getAll();
        //List<CartEntity> carts = this.cartRepository.findAll();

        return cartGenericHttpHelper.getAllItemsResponse(carts, "carritos");
    }

    @Override
    public ResponseEntity<?> getCartById(Long id) {
        CartDto cart = this.cartRepository.getCartObjectById(id);

        return cartGenericHttpHelper.getItemByIdResponse(cart, id, "carrito");
    }

    @Override
    public ResponseEntity<?> getAllProductsOfUser(Long userId) {
        List<ProductCartDto> productsCart = this.cartRepository.getAllProductsFromUser(userId);

        if(productsCart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("no hay productos asignados al usuario de id: "+userId);

        }

        return ResponseEntity.ok(productsCart);
    }

    @Override
    public ResponseEntity<?> updateAmountProduct(Long amount, Long cartId) {
        Optional<CartEntity> cart = this.cartRepository.findById(cartId);

        if(!cart.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no existe un carrito bajo el id: "+cartId);
        }
        else if(amount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cantidad invalida");
        }
        else {
            CartEntity cartEntity = cart.get();
            cartEntity.setAmount(amount);
            this.cartRepository.save(cartEntity);
            CartDto cartDto = new CartDto(cartEntity.getCartId(), cartEntity.getProduct(), cartEntity.getUser(), cartEntity.getAmount(), cartEntity.getCreatedAt(), cartEntity.getUpdatedAt());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(cartDto);
        }
    }

    @Override
    public Long getAmountFromProductOfUser(Long userId, Long productId) {
        Optional<User> user = this.userRepository.findById(userId);
        Optional<ProductEntity> product = this.productRepository.findById(productId);

        Long amount = this.cartRepository.getAmountOfCart(user.get(), product.get());

        return amount;
    }

    @Override
    public Long getCartId(Long userId, Long productId) {
        Optional<User> user = this.userRepository.findById(userId);
        Optional<ProductEntity> product = this.productRepository.findById(productId);
        Long cartId = this.cartRepository.getCartId(user.get(), product.get());

        return cartId;
    }

    @Override
    public ResponseEntity<?> addCart(CartEntity cartEntity) {
        if(cartEntity == null || cartEntity.getProduct() == null || cartEntity.getUser() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("carrito incompleto, fallido");
        }

        Optional<User> user = this.userRepository.findById(cartEntity.getUser().getUserId());
        Optional<ProductEntity> product = this.productRepository.findById(cartEntity.getProduct().getProductId());

        Long cartId = this.cartRepository.getCartId(user.get(), product.get());

        CartDto cart = this.cartRepository.getCartObjectById(cartId);//armar cart entity con todos los datos y objetos

        if(cartId == null) {
            this.cartRepository.save(cartEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(cart);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("el producto ya se encuentra en el carrito");
        }

    }

    @Override
    public ResponseEntity<?> updateCart(Long id, CartEntity newCart) {
        CartDto cart = this.cartRepository.getCartObjectById(id);
        Optional<CartEntity> oldCart = this.cartRepository.findById(id);

        if(!oldCart.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error al actualizar el carrito");
        }

        if(newCart == null || newCart.getProduct() == null || newCart.getUser() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("carrito incompleto, fallido");
        }

        CartEntity cartEntity = oldCart.get();

        ProductEntity product = this.productRepository.findById(newCart.getProduct().getProductId()).get();
        User user = this.userRepository.findById(newCart.getUser().getUserId()).get();

        cartEntity.setAmount(newCart.getAmount());
        cartEntity.setCreatedAt(cartEntity.getCreatedAt());
        cartEntity.setUpdatedAt(newCart.getUpdatedAt());
        cartEntity.setProduct(product);
        cartEntity.setUser(user);

        this.cartRepository.save(cartEntity);

        CartDto cartDto = new CartDto(cartEntity.getCartId(), cartEntity.getProduct(), cartEntity.getUser(), cartEntity.getAmount(), cartEntity.getCreatedAt(), cartEntity.getUpdatedAt());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(cartDto);
    }

    @Override
    public ResponseEntity<?> deleteCart(Long id) {
        Optional<CartEntity> cartToDelete = this.cartRepository.findById(id);

        if(!cartToDelete.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo eliminar el carrito con el id: "+id);
        }

        this.cartRepository.delete(cartToDelete.get());

        CartDto cartDto = new CartDto(cartToDelete.get().getCartId(), cartToDelete.get().getProduct(), cartToDelete.get().getUser(), cartToDelete.get().getAmount(), cartToDelete.get().getCreatedAt(), cartToDelete.get().getUpdatedAt());

        return ResponseEntity.ok(cartToDelete);
    }

    @Override
    public ResponseEntity<?> cleanCart(Long userId) {
        if(userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usuario invalido");
        }

        User user = this.userRepository.findById(userId).orElse(null);

        List<CartEntity> carts = this.cartRepository.getCartsByUserId(user);

        if(carts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("el carrito no existe");
        }

        this.cartRepository.deleteAll(carts);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(carts);
    }
}
