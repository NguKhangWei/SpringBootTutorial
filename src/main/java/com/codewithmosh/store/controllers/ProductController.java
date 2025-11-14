package com.codewithmosh.store.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<ProductDto> getAllProducts(
        @RequestParam(name = "categoryId", required = false) Byte categoryId
    ){
        List<Product> products;
        if (categoryId != null){
            products = this.productRepository.findByCategoryId(categoryId);
        }else{
            products = this.productRepository.findAllWithCategory();
        }
        return products.stream()
                       .map(productMapper::toDto)
                       .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
        var product = this.productRepository.findById((id)).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(this.productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
        @RequestBody ProductDto productDto,
        UriComponentsBuilder uriBuilder
    ){
        var category = this.categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null){
            return ResponseEntity.badRequest().build();
        }

        var product = this.productMapper.toEntity(productDto);
        product.setCategory(category);
        this.productRepository.save(product);
        productDto.setId(product.getId());

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
        @PathVariable Long id,
        @RequestBody ProductDto productDto
    ){
        var category = this.categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null){
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        var product = productRepository.findById(id).orElse(null);
        if (product == null){
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
