package org.fxee.osgi.spring.test.components;

import org.fxee.osgi.spring.annotations.ComponentImport;
import org.fxee.osgi.spring.test.services.TestService1;
import org.fxee.osgi.spring.test.services.TestServiceConstructor;
import org.springframework.stereotype.Component;

@Component
public class TestComponent1 {

    @ComponentImport
    private TestService1 testService1;

    private TestServiceConstructor testServiceConstructor;

    public TestComponent1() {
    }

    public TestComponent1(@ComponentImport TestServiceConstructor serviceConstructor) {
        this.testServiceConstructor = serviceConstructor;
    }

    public String getServiceResult() {
        return testService1.printTestMessage();
    }

    public String getConstructorServiceResult() {
        return testServiceConstructor.printMessage();
    }

}
