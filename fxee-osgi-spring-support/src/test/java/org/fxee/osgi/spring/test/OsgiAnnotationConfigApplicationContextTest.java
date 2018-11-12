package org.fxee.osgi.spring.test;

import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.fxee.osgi.spring.context.OsgiAnnotationConfigApplicationContext;
import org.fxee.osgi.spring.context.OsgiAnnotationConfigApplicationContextFactory;
import org.fxee.osgi.spring.test.components.TestComponent1;
import org.fxee.osgi.spring.test.components.TestComponent2;
import org.fxee.osgi.spring.test.config.TestModuleConfig;
import org.fxee.osgi.spring.test.services.TestService1;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.osgi.framework.BundleContext;


@RunWith(JUnit4ClassRunner.class)
public class OsgiAnnotationConfigApplicationContextTest {

    private String basePackage = "org.fxee.osgi.spring.test.components";

    private BundleContext bundleContext;
    private OsgiAnnotationConfigApplicationContextFactory factory;

    @Before
    public void init() {
        bundleContext =  MockOsgi.newBundleContext();
        factory = new OsgiAnnotationConfigApplicationContextFactory();
        bundleContext.registerService(TestService1.class, new TestService1(), null);
    }

    @Test
    public void scanOsgiBasePackages() {
        OsgiAnnotationConfigApplicationContext osgiAnnotationConfigApplicationContext = new OsgiAnnotationConfigApplicationContext(TestModuleConfig.class);
        osgiAnnotationConfigApplicationContext.scanOsgiBasePackages(new String[] {basePackage});
        osgiAnnotationConfigApplicationContext.refresh();
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestComponent1.class), null);
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestComponent2.class), null);
    }

    @Test
    public void doScanAnnotatedOsgiComponent() {
        OsgiAnnotationConfigApplicationContext osgiAnnotationConfigApplicationContext = new OsgiAnnotationConfigApplicationContext(TestModuleConfig.class);
        osgiAnnotationConfigApplicationContext.refresh();
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestComponent1.class), null);
        Assert.assertNotEquals(osgiAnnotationConfigApplicationContext.getBean(TestComponent2.class), null);
    }

    //TODO - test factory beans processors


}