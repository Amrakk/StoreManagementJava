package com.StoreManagementClient.Models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class Product {
    private String pid;
    private String name;
    private Category category;
    private double importPrice;
    private double retailPrice;
    private String barcode;
    private String illustrator;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
