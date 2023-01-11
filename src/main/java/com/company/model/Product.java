package com.company.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Product {
    private Integer id;
    private String name;
    private Double price;
    private String image;

    private boolean is_deleted = false;

    public Product(String name, Double price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;

    }

    public Product(Integer id) {
        this.id = id;
    }
}
