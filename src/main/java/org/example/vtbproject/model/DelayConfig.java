package org.example.vtbproject.model;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DelayConfig {

    private final AtomicInteger responseDelay = new AtomicInteger(0);

    public int getResponseDealy() {
        return responseDelay.get();
    }

    public void setResponseDelay(int ms) {
        responseDelay.set(ms);
    }
}
