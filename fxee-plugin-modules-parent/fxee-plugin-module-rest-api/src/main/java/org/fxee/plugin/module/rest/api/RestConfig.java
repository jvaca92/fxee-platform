package org.fxee.plugin.module.rest.api;

import org.fxee.osgi.api.ModuleConfig;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Configuration class as register for rest module resources
 * @author Jan Vaca <jan.vaca92@gmail.com> on 12/2/2018
 */
public class RestConfig implements ModuleConfig {

    private Collection<Class<?>> resources;

    public RestConfig() {
        this.resources = Collections.emptyList();
    }

    @Override
    public ModuleConfig register(Class<?> resource) {
        resources.add(resource);
        return this;
    }

    @Override
    public ModuleConfig unregisterAll() {
        resources.clear();
        return this;
    }

    @Override
    public ModuleConfig unregister(Class<?> resource) {
        resources.remove(resource);
        return this;
    }

    @Override
    public Collection<Class<?>> getAllResources() {
        return resources;
    }
}
