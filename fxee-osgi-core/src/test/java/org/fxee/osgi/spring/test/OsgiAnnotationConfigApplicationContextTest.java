package org.fxee.osgi.spring.test;

import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.fxee.osgi.context.AnnotationPluginContext;
import org.fxee.osgi.context.AnnotationWebPluginContext;
import org.fxee.osgi.spring.test.beans.DefaultExposeBean;
import org.fxee.osgi.spring.test.components.TestComponent1;
import org.fxee.osgi.spring.test.components.TestComponent2;
import org.fxee.osgi.spring.test.config.TestPluginConfig;
import org.fxee.osgi.spring.test.controllers.TestRestController;
import org.fxee.osgi.spring.test.controllers.TestController;
import org.fxee.osgi.spring.test.components.ExposeComponent;
import org.fxee.osgi.spring.test.services.TestService1;
import org.fxee.osgi.spring.test.services.TestServiceConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


@RunWith(JUnit4ClassRunner.class)
public class OsgiAnnotationConfigApplicationContextTest {

    private String[] basePackages = new String[] { "org.fxee.osgi.spring.test.components", "org.fxee.osgi.spring.test.controllers"};

    private BundleContext bundleContext;

    @Before
    public void init() {
        bundleContext =  MockOsgi.newBundleContext();
        bundleContext.registerService(TestService1.class, new TestService1(), null);
        bundleContext.registerService(TestServiceConstructor.class, new TestServiceConstructor(), null);
    }


    @Test(expected = NoSuchBeanDefinitionException.class)
    public void scanOsgiComponents() {
        AnnotationPluginContext annotationConfigPluginContext = new AnnotationPluginContext(bundleContext, new AnnotationConfigApplicationContext(), TestPluginConfig.class);
        annotationConfigPluginContext.scanOsgiBasePackages(basePackages);
        annotationConfigPluginContext.refresh();
        Assert.assertNotEquals(annotationConfigPluginContext.getComponent(TestComponent1.class), null);
        Assert.assertNotEquals(annotationConfigPluginContext.getComponent(TestComponent2.class), null);
        Assert.assertNull(annotationConfigPluginContext.getComponent(TestController.class));
        Assert.assertNull(annotationConfigPluginContext.getComponent(TestRestController.class));
    }

    @Test
    public void scanOsgiWebCompnents() {
        AnnotationWebPluginContext osgiAnnotationConfigApplicationContext = new AnnotationWebPluginContext(bundleContext, new AnnotationConfigWebApplicationContext(), TestPluginConfig.class);
        osgiAnnotationConfigApplicationContext.scanOsgiBasePackages(basePackages);
        osgiAnnotationConfigApplicationContext.refresh();
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getComponent(TestComponent1.class), null);
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getComponent(TestComponent2.class), null);
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getComponent(TestController.class), null);
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getComponent(TestRestController.class), null);
    }

    @Test
    public void doScanAnnotatedOsgiComponent() {
        AnnotationPluginContext annotationConfigPluginContext = new AnnotationPluginContext(bundleContext, new AnnotationConfigApplicationContext(), TestPluginConfig.class);
        annotationConfigPluginContext.refresh();
        Assert.assertNotEquals(annotationConfigPluginContext.getComponent(TestComponent1.class), null);
        Assert.assertNotEquals(annotationConfigPluginContext.getComponent(TestComponent2.class), null);
    }

    @Test
    public void osgiComponentBeanFactoryPostProcessorTest() {
        AnnotationWebPluginContext osgiAnnotationConfigApplicationContext = new AnnotationWebPluginContext(bundleContext, new AnnotationConfigWebApplicationContext(), TestPluginConfig.class);
        osgiAnnotationConfigApplicationContext.refresh();
        TestComponent1 testComponent1 = osgiAnnotationConfigApplicationContext.getComponent(TestComponent1.class);
        TestComponent2 testComponent2 = osgiAnnotationConfigApplicationContext.getComponent(TestComponent2.class);
        Assert.assertNotEquals(testComponent1.getServiceResult(), null);
        Assert.assertNotEquals(testComponent2.getServiceResult(), null);
        Assert.assertNotEquals(testComponent1.getConstructorServiceResult(), null);
        Assert.assertNotEquals(testComponent2.getConstructorServiceResult(), null);
    }

    @Test
    public void exposeOsgiComponentsTest() {
        AnnotationPluginContext annotationConfigPluginContext = new AnnotationPluginContext(bundleContext, new AnnotationConfigApplicationContext(), TestPluginConfig.class);
        annotationConfigPluginContext.refresh();
        annotationConfigPluginContext.exposeServices();
        ServiceReference reference = bundleContext.getServiceReference(ExposeComponent.class);
        ServiceReference referenceBean = bundleContext.getServiceReference(DefaultExposeBean.class);
        Assert.assertNotEquals(bundleContext.getService(reference), null);
        Assert.assertNotEquals(bundleContext.getService(referenceBean), null);
    }


}