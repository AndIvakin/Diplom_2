package com.praktikum;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
    private int number;
    private String _id;
    private List<IngredientsList> ingredients;
    private Owner owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int price;
}
