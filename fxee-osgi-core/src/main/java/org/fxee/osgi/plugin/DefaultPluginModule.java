package org.fxee.osgi.plugin;

/**
 * Default implementation of plugin module
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/25/2018
 */
public class DefaultPluginModule implements PluginModule {

    private String type;

    private String key;

    private String name;


    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }
}
