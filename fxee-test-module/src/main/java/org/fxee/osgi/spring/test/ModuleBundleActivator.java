package org.fxee.osgi.spring.test;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class ModuleBundleActivator implements BundleActivator {

    private static final Logger  LOG = LoggerFactory.getLogger(ModuleBundleActivator.class);

    private HttpService httpService;
    private AnnotationConfigWebApplicationContext webApplicationContext;
    Set<Class> candiateComponents = new HashSet<Class>();
    String basePackage = "org.fxee.test";

    public void start(BundleContext bundleContext) throws Exception {

        LOG.info("Starting processors with name {}", bundleContext.getBundle().getSymbolicName());

        ServiceReference ref =  bundleContext.getServiceReference(HttpService.class.getName());
        httpService = (HttpService) bundleContext.getService(ref);

        ScanResult scanResult = new ClassGraph().addClassLoader(this.getClass().getClassLoader())
                                                .whitelistPackages(basePackage)
                                                .enableAllInfo()
                                                .scan();
        /*** Just  for info ***/
        scanResult.getAllClasses()
                .getStandardClasses()
                .loadClasses()
                .stream()
//                .filter(clazz -> clazz.isAnnotationPresent(Component.class) ||
//                        clazz.isAnnotationPresent(Service.class) ||
//                        clazz.isAnnotationPresent(Repository.class) ||
//                        clazz.isAnnotationPresent(Controller.class) ||
//                        clazz.isAnnotationPresent(Configuration.class))
                .forEach( clazz -> {
                    candiateComponents.add(clazz);
                    LOG.info("Was scanned clazz with name {}", clazz.getName());
                        });


//        OsgiBundleResourcePatternResolver bundleResourcePatternResolver = new OsgiBundleResourcePatternResolver(bundleContext.getBundle());

//        Resource[] resources = bundleResourcePatternResolver.getResources("classpath:");
//
//        for(Resource resource : resources) {
//            LOG.info("The resource filename: {}", resource.getFilename());
//            LOG.info("The resource URL: {}", resource.getURL().toExternalForm());
//            LOG.info("The resource file absolute path: {}", resource.getFile().getAbsolutePath());
//        }
          WebApplicationContext applicationContext = getOrCreateWebApplicationContext(candiateComponents);
          // this is to register the controller
          httpService.registerServlet("/test/*",  createServlet(applicationContext), new Hashtable(), null);

//        Dictionary httpContextProperty = new Hashtable();
//        httpContextProperty.put(ExtenderConstants.PROPERTY_HTTP_CONTEXT_ID, "httpModule");
//
//        Dictionary servletProperty = new Hashtable();
//        servletProperty.put(ExtenderConstants.PROPERTY_ALIAS, "/test" );
//        servletProperty.put(ExtenderConstants.PROPERTY_HTTP_CONTEXT_ID, "httpModule");
//
//        DefaultListenerMapping listenerMapping = new DefaultListenerMapping();
//        listenerMapping.setHttpContextId("httpModule");
//        listenerMapping.setListener(new ContextLoaderListener(applicationContext));
//
//        HashMap<String, String> contextMappingParams = new HashMap<String, String>();
//        contextMappingParams.put(ExtenderConstants.PROPERTY_HTTP_VIRTUAL_HOSTS,
//                "localhost");
//        contextMappingParams.put(ExtenderConstants.PROPERTY_HTTP_CONNECTORS,
//                "jettyConn1");
//
//        DefaultHttpContextMapping contextMapping = new DefaultHttpContextMapping();
//        contextMapping.setHttpContextId("httpModule");
//        contextMapping.setPath("/test");
//        contextMapping.setParameters(contextMappingParams);
//
//        bundleContext.registerService(HttpContextMapping.class, contextMapping , httpContextProperty);
//        bundleContext.registerService(Servlet.class, createServlet(applicationContext), servletProperty);
//        bundleContext.registerService(ListenerMapping.class, listenerMapping, null);

        LOG.info("Dispatcher servlet successfully registered");
    }

    public void stop(BundleContext bundleContext) throws Exception {

        httpService.unregister("/test/*");
        LOG.debug("Dispatcher servlet successfully unregistered");
        webApplicationContext.stop();
        LOG.debug("Web application context was successfullu closed");
    }

    protected Servlet createServlet(WebApplicationContext applicationContext) {
        DispatcherServlet dispatcherServlet =  new DispatcherServlet();
        dispatcherServlet.setApplicationContext(applicationContext);
        dispatcherServlet.refresh();
        return dispatcherServlet;
    }

    protected WebApplicationContext getOrCreateWebApplicationContext(Set<Class> candiateComponents) {
        if(webApplicationContext == null) {
            webApplicationContext = new AnnotationConfigWebApplicationContext();
        }
        webApplicationContext.setClassLoader(this.getClass().getClassLoader());
        //Register component candidates
        candiateComponents.forEach(candiateComponent -> webApplicationContext.register(candiateComponent));
        webApplicationContext.refresh();
        webApplicationContext.start();

        return webApplicationContext;
    }
}
