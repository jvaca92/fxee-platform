package org.fxee.osgi.spring.test.controllers;


import org.fxee.osgi.spring.test.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModuleController {


    @Autowired
    private ModuleService moduleService;

    @RequestMapping(path= "/message", method = RequestMethod.GET)
    public String printModuleTestMessage() {
        return moduleService.printModuleName();
    }
}
