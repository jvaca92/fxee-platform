package org.fxee.osgi.spring.test.services;

import java.util.UUID;

public class TestService1 {

    public String printTestMessage() {
        return UUID.randomUUID().toString();
    }
}
