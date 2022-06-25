package io.oferto.microservice.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import io.oferto.microservice.common.ProductDto;
import io.oferto.microservice.exception.ProductNotFoundException;
import io.oferto.microservice.exception.ProductUnSupportedFieldPatchException;
import io.oferto.microservice.model.Product;
import io.oferto.microservice.respository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

@RestController
public class ProductController {
	@Autowired
    private ProductRepository productRepository;
	
    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping("/products")
    List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        
        return products.stream()
          .map(this::convertToDto)
          .collect(Collectors.toList());
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/products")
    ProductDto save(@RequestBody ProductDto productDto) {
    	Product product = convertToEntity(productDto);
    	
    	Product productCreated = productRepository.save(product);
        
    	return convertToDto(productCreated);
    }
    
    @GetMapping("/products/{id}")
    ProductDto findOne(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        
        return convertToDto(product);
    }
    
    @PutMapping("/products/{id}")
    ProductDto saveOrUpdate(@RequestBody Product product, @PathVariable Long id) {

        Product _product = productRepository.findById(id)
                .map(x -> {
                    x.setName(product.getName());
                    x.setDescription(product.getDescription());
                    x.setPrice(product.getPrice());
                    
                    return productRepository.save(x);
                })
                .orElseGet(() -> {
                	product.setId(id);
                    return productRepository.save(product);
                });
        
        return convertToDto(_product);
    }
    
    @PatchMapping("/products/{id}")
    ProductDto patch(@RequestBody Map<String, String> update, @PathVariable Long id) {

        Product product = productRepository.findById(id)
                .map(x -> {
                	String price = update.get("price");
                	
                    if (!StringUtils.isEmpty(price)) {
                        x.setPrice(new BigDecimal(price));

                        // better create a custom method to update a value = :newValue where id = :id
                        return productRepository.save(x);
                    } else {
                        throw new ProductUnSupportedFieldPatchException(update.keySet());
                    }

                })
                .orElseGet(() -> {
                    throw new ProductNotFoundException(id);
                });
        
        return convertToDto(product);

    }
    
    @DeleteMapping("/products/{id}")
    void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
    
    private ProductDto convertToDto(Product product) {
    	ProductDto productDto = modelMapper.map(product, ProductDto.class);
    	    	
    	return productDto;
    }
    
    private Product convertToEntity(ProductDto productDto) throws ParseException {
    	Product product = modelMapper.map(productDto, Product.class);
    	        
        return product;
    }
}
