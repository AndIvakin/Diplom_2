package com.praktikum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    public final List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, List<String>> inputDataMapForOrder() {
        Map<String, List<String>> inputDataMap = new HashMap<>();
        inputDataMap.put("ingredients", ingredients);
        return inputDataMap;
    }
}
