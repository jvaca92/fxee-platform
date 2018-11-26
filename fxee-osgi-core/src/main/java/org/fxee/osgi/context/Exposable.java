package org.fxee.osgi.context;

/**
 * Interface to handle exposing or disposing of beans from application context as services into service registry
 *
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/18/2018
 */
public interface Exposable {

    void exposeServices();

    void disposeServices();
}
