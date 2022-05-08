package com.praktikum;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderList {
    private List<String> ingredients;
    private String _id;
    private String status;
    private String name;
    private int number;
    private String createdAt;
    private String updatedAt;
}
