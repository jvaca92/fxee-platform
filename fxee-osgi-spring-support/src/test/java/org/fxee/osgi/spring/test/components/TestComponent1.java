package org.fxee.osgi.spring.test.components;

import org.fxee.osgi.spring.annotations.ComponentImport;
import org.fxee.osgi.spring.test.services.TestService1;
import org.springframework.stereotype.Component;

@Component
public class TestComponent1 {

    @ComponentImport
    private TestService1 testService1;

    public String getServiceResult() {
        return testService1.printTestMessage();
    }
}
