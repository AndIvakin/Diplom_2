package com.praktikum;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrdersResponse {
    private boolean success;
    private List<OrderList> orders;
    private int total;
    private int totalToday;
}
