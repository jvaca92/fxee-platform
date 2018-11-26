package org.fxee.osgi.spring.test.components;

import org.fxee.osgi.api.annotations.annotations.ComponentImport;
import org.fxee.osgi.spring.test.services.TestService1;
import org.fxee.osgi.spring.test.services.TestServiceConstructor;
import org.springframework.stereotype.Component;

@Component
public class TestComponent2 {

    @ComponentImport
    private TestService1 testService1;

    private TestServiceConstructor testServiceConstructor;

    public TestComponent2() {
    }

    @ComponentImport
    public TestComponent2(TestServiceConstructor testServiceConstructor) {
        this.testServiceConstructor = testServiceConstructor;
    }

    public String getServiceResult() {
        return testService1.printTestMessage();
    }

    public String getConstructorServiceResult() {
        return testServiceConstructor.printMessage();
    }
}
