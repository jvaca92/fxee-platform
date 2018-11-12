package org.fxee.osgi.spring.test;


import org.fxee.osgi.spring.annotations.Module;
import org.fxee.osgi.spring.test.beans.TestBean1;
import org.fxee.osgi.spring.test.beans.TestBean2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
public class TestModuleConfigBeans {

    @Module(name = "test-module")
    static class ContextConfiguration {

        @Bean
        public TestBean1  testBean1() {
            TestBean1 testBean1 = new TestBean1();
            return testBean1;
        }

        @Bean
        public TestBean2 testBean2() {
            TestBean2 testBean2 = new TestBean2();
            return testBean2;
        }
    }

    @Autowired
    private TestBean1 testBean1;

    @Autowired
    TestBean2 testBean2;

    @Test
    public void testTestBean1() {
        Assert.assertEquals(testBean1.getClass(), TestBean1.class);
    }

    @Test
    public void testTestBean2() {
        Assert.assertEquals(testBean2.getClass(), TestBean2.class);
    }
}
