package org.fxee.osgi.plugin;

import org.fxee.osgi.context.PluginContext;
import org.osgi.framework.ServiceRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of plugin class which contains main functions of fxee plugin
 * TODO - describe more details
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/24/2018
 */
public class DefaultPlugin implements Plugin {

    /**
     * List of plugin modules info
     */
    private List<ServiceRegistration<PluginModule>> pluginModules = new ArrayList<>();

    /**
     * Main plugin context
     */
    private PluginContext pluginContext;

    /**
     * Plugin key
     */
    private String key;

    /**
     * Plugin name
     */
    private String name;

    /**
     * Plugin description
     */
    private String desc;

    private DefaultPlugin(Builder builder) {
        this.pluginModules = builder.pluginModules;
        this.pluginContext = builder.pluginContext;
        this.key = builder.key;
        this.name = builder.name;
        this.desc = builder.desc;
    }

    public static Builder newDefaultPlugin() {
        return new Builder();
    }


    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public List<ServiceRegistration<PluginModule>> getPluginModules() {
        return pluginModules;
    }

    @Override
    public PluginContext getPluginContext() {
        return pluginContext;
    }

    @Override
    public void destroyPluginModules() {
        pluginModules.forEach(pluginModule -> pluginModule.unregister());
    }

    public void setPluginModules(List<ServiceRegistration<PluginModule>> pluginModules) {
        this.pluginModules = pluginModules;
    }

    public void setPluginContext(PluginContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public static final class Builder {
        private List<ServiceRegistration<PluginModule>> pluginModules;
        private PluginContext pluginContext;
        private String key;
        private String name;
        private String desc;

        private Builder() {
        }

        public DefaultPlugin build() {
            return new DefaultPlugin(this);
        }

        public Builder pluginModules(List<ServiceRegistration<PluginModule>> pluginModules) {
            this.pluginModules = pluginModules;
            return this;
        }

        public Builder pluginContext(PluginContext pluginContext) {
            this.pluginContext = pluginContext;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder desc(String desc) {
            this.desc = desc;
            return this;
        }
    }
}
