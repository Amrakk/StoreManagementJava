package com.finalproject.storemanagementproject.controllers;

import com.finalproject.storemanagementproject.models.APIResponse;
import com.finalproject.storemanagementproject.models.Order;
import com.finalproject.storemanagementproject.models.Product;
import com.finalproject.storemanagementproject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProduct();
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Optional<Product>>> getProductById(@PathVariable String id) {

        Optional<Product> product = productService.getProductById(id);

        Integer HTTP_CODE = HttpStatus.OK.value();
        String message = "Success";

        if (product == null) {
            HTTP_CODE = HttpStatus.NOT_FOUND.value();
            message = "Not found product";
        }

        return ResponseEntity.ok(new APIResponse<>(HTTP_CODE, message, Collections.singletonList(product)));
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse<Product>> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.CREATED.value(), "Product has been created successfully",
                Collections.singletonList(savedProduct)));
    }

    @PostMapping("update/{id}")
    public ResponseEntity<APIResponse<Product>> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
        Product product = productService.getProductById(id).orElse(null);

        Integer HTTP_CODE = HttpStatus.OK.value();
        String message = "Update Success";

        if (product != null) {
            product.setName(updatedProduct.getName());
            product.setCategory(updatedProduct.getCategory());
            product.setRetailPrice(updatedProduct.getRetailPrice());
            product.setUpdatedAt(updatedProduct.getUpdatedAt());
            productService.saveProduct(product);
            return ResponseEntity.ok(new APIResponse<>(HTTP_CODE, message, Collections.singletonList(product)));
        }

        HTTP_CODE = HttpStatus.NOT_FOUND.value();
        message = "Not found product";

        return ResponseEntity.ok(new APIResponse<>(HTTP_CODE, message, Collections.singletonList(product)));
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<APIResponse<Void>> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Delete Success", null));
    }
}
