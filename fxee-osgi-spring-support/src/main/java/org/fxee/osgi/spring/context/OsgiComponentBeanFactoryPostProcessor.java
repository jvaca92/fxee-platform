package org.fxee.osgi.spring.context;

import org.fxee.osgi.spring.annotations.ComponentImport;
import org.fxee.osgi.utils.OsgiUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * Post bean factory processor which manage extension instance from registry
 * and assign to variable which are marked by annotation {@link ComponentImport}
 */
public class OsgiComponentBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(OsgiComponentBeanFactoryPostProcessor.class);

    private final BundleContext bundleContext;

    public OsgiComponentBeanFactoryPostProcessor(BundleContext bundleContext) {
        this.bundleContext = bundleContext; }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        LOG.debug("Starting bean factory post process for osgi components");
        String[] beanClasses = beanFactory.getBeanDefinitionNames();
        Stream.of(beanClasses)
                .forEach(beanName -> {
                    LOG.debug("Is processing bean with name {}", beanName);
                    Object beanInstance = beanFactory.getBean(beanName);
                    Collection<String> constructorOsgiComponents = getConstructorOsgiComponents(beanInstance.getClass());
                    ReflectionUtils.doWithFields(beanInstance.getClass(), new ComponentImportFieldCallback(bundleContext, beanInstance, constructorOsgiComponents));
                });
        LOG.debug("Stopping post process bean factory for osgi components");
    }


    /**
     * Find all osgi component which are located in constructor and are annotated by {@link ComponentImport}
     * @param bean - bean class
     * @return - collection of osgi components
     */
    protected Collection<String> getConstructorOsgiComponents(Class<?> bean) {
        LOG.debug("Staring finding osgi components in constructor params of bean {}", bean.getName());
        Collection<String> constructorComponents = new LinkedHashSet<>();
        Stream.of(bean.getConstructors())
                .forEach(constructor -> {
                    if(constructor.isAnnotationPresent(ComponentImport.class)) {
                        LOG.debug("Find whole constructor annotated by annotation @ComponentImport so each constructor will be processed");
                        constructorComponents.addAll(
                                Stream.of(constructor.getParameterTypes())
                                        .map(typeParam -> typeParam.getName())
                                        .collect(Collectors.toSet())
                        );
                    } else {
                        LOG.debug("Each constructor param will be check if annotated by @ComponentImport");
                        Stream.of(constructor.getParameters())
                                .forEach(param -> {
                                    if(param.isAnnotationPresent(ComponentImport.class)) {
                                        constructorComponents.add(param.getType().getName());
                                        LOG.debug("Constructor param {} is annotated by annotation @ComponentImport",param.getName());
                                    }
                                });
                    }
                });
        LOG.debug("Stopping finding osgi components in constructor params of bean {}", bean.getName());

        return constructorComponents;
    }
}

