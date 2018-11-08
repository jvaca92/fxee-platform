package org.fxee.test.services;


import org.springframework.stereotype.Component;

@Component
public class ModuleService {

    private String MODULE_NAME = "TEST MODULE";

    public String printModuleName() {
        return MODULE_NAME;
    }
 }
