package org.fxee.osgi.spring.test.beans;

import java.util.UUID;

/**
 *
 * @author Jan Vaca <jan.vaca92@gmail.com> on 11/22/2018
 */
public class DefaultExposeBean {

    public String testMessage() {
        return UUID.randomUUID().toString();
    }
}
