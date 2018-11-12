package org.fxee.osgi.spring.context;

import org.fxee.osgi.spring.annotations.ComponentImport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.StandardMethodMetadata;

/***
 * Post bean factory processor which manage extension instance from registry
 * and assign to variable which are marked by annotation {@link ComponentImport}
 */
public class OsgiComponentBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(OsgiComponentBeanFactoryPostProcessor.class);

    private final BundleContext bundleContext;

    public OsgiComponentBeanFactoryPostProcessor(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            StandardAnnotationMetadata metadata = (StandardAnnotationMetadata) (beanDefinition.getSource());
            if (metadata.hasAnnotation(ComponentImport.class.getName())) {
                // 1. remove abstract definition
                beanFactory.removeBeanDefinition(beanDefinitionName);
                // 2. get service from osgi
//                String serviceClassName = metadata.getIntrospectedMethod().getReturnType().getCanonicalName();
//                ServiceReference reference = bundleContext.getServiceReference(serviceClassName);
//                Object instance = bundleContext.getService(reference);
                //Register
//                beanFactory.registerSingleton(metadata.getMethodName(), instance);
            }
            }
    }


}

