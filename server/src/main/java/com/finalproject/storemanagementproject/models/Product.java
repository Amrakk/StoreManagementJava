package com.finalproject.storemanagementproject.models;

import java.time.LocalDateTime;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "products")
@Data
@Getter
@Setter
public class Product {
    @Id
    private String pid;
    private String name;
    private Category category;
    private double importPrice;
    private double retailPrice;
    @Indexed(unique = true)
    private String barcode;
    private String illustrator;
    private int quantity;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
