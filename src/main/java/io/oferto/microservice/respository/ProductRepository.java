package io.oferto.microservice.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.oferto.microservice.model.Product;

public interface ProductRepository  extends JpaRepository<Product, Long> {

}
