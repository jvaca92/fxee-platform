package org.fxee.osgi.bundle;

import org.fxee.osgi.annotations.ExtensionImport;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.type.StandardMethodMetadata;

/***
 * Post bean factory processor which manage extension instance from registry
 * and assign to variable which are marked by annotation {@link org.fxee.osgi.annotations.ExtensionImport}
 */
public class OsgiComponentBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(OsgiComponentBeanFactoryPostProcessor.class);

    private final BundleContext bundleContext;

    public OsgiComponentBeanFactoryPostProcessor(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;

        // all bean definitions
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            if (beanDefinition.getSource() instanceof StandardMethodMetadata) {
                StandardMethodMetadata metadata = (StandardMethodMetadata) (beanDefinition.getSource());
                if (metadata.isAnnotated(ExtensionImport.class.getCanonicalName())) {
                    // 1. remove abstract definition
                    beanFactory.removeBeanDefinition(beanDefinitionName);
                    // 2. get service from osgi
                    String extensionClassName = metadata.getIntrospectedMethod().getReturnType().getCanonicalName();
                    ServiceTracker serviceTracker = new ServiceTracker(bundleContext, extensionClassName, null);
                    // 3. register service as singleton
//                    beanFactory.registerSingleton(metadata.getMethodName(), serviceTracker.waitForService(0));
                }
            }
        }
    }



}
