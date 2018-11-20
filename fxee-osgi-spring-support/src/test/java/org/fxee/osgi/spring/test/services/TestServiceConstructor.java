package org.fxee.osgi.spring.test.services;


import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TestServiceConstructor {

    public String printMessage() {
        return UUID.randomUUID().toString();
    }
}
