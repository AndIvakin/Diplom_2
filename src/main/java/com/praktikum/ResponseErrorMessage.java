package com.praktikum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseErrorMessage {
    private boolean success;
    private String message;

    @Override
    public String toString() {
        return "{" +
                "success=" + success +
                ", message=" + message +
                "}";
    }
}
