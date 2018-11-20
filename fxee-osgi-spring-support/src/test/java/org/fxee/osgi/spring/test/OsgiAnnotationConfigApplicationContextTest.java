package org.fxee.osgi.spring.test;

import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.fxee.osgi.spring.context.OsgiAnnotationConfigApplicationContext;
import org.fxee.osgi.spring.context.OsgiAnnotationConfigWebApplicationContext;
import org.fxee.osgi.spring.context.OsgiApplicationContextFactory;
import org.fxee.osgi.spring.test.components.TestComponent1;
import org.fxee.osgi.spring.test.components.TestComponent2;
import org.fxee.osgi.spring.test.config.TestPluginConfig;
import org.fxee.osgi.spring.test.controllers.TestRestController;
import org.fxee.osgi.spring.test.controllers.TestController;
import org.fxee.osgi.spring.test.services.TestService1;
import org.fxee.osgi.spring.test.services.TestServiceConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;


@RunWith(JUnit4ClassRunner.class)
public class OsgiAnnotationConfigApplicationContextTest {

    private String[] basePackages = new String[] { "org.fxee.osgi.spring.test.components", "org.fxee.osgi.spring.test.controllers"};

    private BundleContext bundleContext;
    private OsgiApplicationContextFactory factory;

    @Before
    public void init() {
        bundleContext =  MockOsgi.newBundleContext();
        factory = new OsgiApplicationContextFactory();
        bundleContext.registerService(TestService1.class, new TestService1(), null);
        bundleContext.registerService(TestServiceConstructor.class, new TestServiceConstructor(), null);
    }


    @Test(expected = NoSuchBeanDefinitionException.class)
    public void scanOsgiComponents() {
        OsgiAnnotationConfigApplicationContext osgiAnnotationConfigApplicationContext = new OsgiAnnotationConfigApplicationContext(TestPluginConfig.class);
        osgiAnnotationConfigApplicationContext.scanOsgiBasePackages(basePackages);
        osgiAnnotationConfigApplicationContext.refresh();
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestComponent1.class), null);
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestComponent2.class), null);
        Assert.assertNull(osgiAnnotationConfigApplicationContext.getBean(TestController.class));
        Assert.assertNull(osgiAnnotationConfigApplicationContext.getBean(TestRestController.class));
    }

    @Test
    public void scanOsgiWebCompnents() {
        OsgiAnnotationConfigWebApplicationContext osgiAnnotationConfigApplicationContext = new OsgiAnnotationConfigWebApplicationContext(TestPluginConfig.class);
        osgiAnnotationConfigApplicationContext.scanOsgiBasePackages(basePackages);
        osgiAnnotationConfigApplicationContext.refresh();
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestComponent1.class), null);
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestComponent2.class), null);
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestController.class), null);
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestRestController.class), null);
    }

    @Test
    public void doScanAnnotatedOsgiComponent() {
        OsgiAnnotationConfigApplicationContext osgiAnnotationConfigApplicationContext = new OsgiAnnotationConfigApplicationContext(TestPluginConfig.class);
        osgiAnnotationConfigApplicationContext.refresh();
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestComponent1.class), null);
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestComponent2.class), null);
    }

    @Test
    public void osgiComponentBeanFactoryPostProcessorTest() {
        OsgiAnnotationConfigWebApplicationContext applicationContext = factory.createWebApplicationContext(bundleContext, TestPluginConfig.class);
        applicationContext.refresh();
        TestComponent1 testComponent1 = applicationContext.getBean(TestComponent1.class);
        TestComponent2 testComponent2 = applicationContext.getBean(TestComponent2.class);
        Assert.assertNotEquals(testComponent1.getServiceResult(), null);
        Assert.assertNotEquals(testComponent2.getServiceResult(), null);
        Assert.assertNotEquals(testComponent1.getConstructorServiceResult(), null);
        Assert.assertNotEquals(testComponent2.getConstructorServiceResult(), null);
    }


}