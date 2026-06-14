package org.example.vtbproject.model;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;


@Component
public class ActivateConfig {

    private final AtomicBoolean serviceActivity = new AtomicBoolean(false);

    public boolean isActivated() {
        return serviceActivity.get();
    }

    public void setActivate(boolean unavailable) {
        this.serviceActivity.set(unavailable);
    }

}
