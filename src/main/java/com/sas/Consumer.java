package com.sas;

import org.springframework.stereotype.Component;

@Component
public class Consumer {

    public String handleMessage(String message) {
        return message.toUpperCase();
    }

    public String handleMessage(byte[] bytes) {
        return handleMessage(new String(bytes));
    }
}