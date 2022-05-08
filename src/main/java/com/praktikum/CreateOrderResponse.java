package com.praktikum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderResponse {
    private String name;
    private OrderResponse order;
    private boolean success;
}
