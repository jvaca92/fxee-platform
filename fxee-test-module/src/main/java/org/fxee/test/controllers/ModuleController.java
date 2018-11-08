package org.fxee.test.controllers;


import org.fxee.test.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ModuleController {


    @Autowired
    private ModuleService moduleService;

    @RequestMapping(path= "/message", method = RequestMethod.GET)
    public String printModuleTestMessage() {
        return moduleService.printModuleName();
    }
}
