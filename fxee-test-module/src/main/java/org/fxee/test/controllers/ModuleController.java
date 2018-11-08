package org.fxee.test.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ModuleController {

    @RequestMapping(path= "/message", method = RequestMethod.GET)
    public String printModuleTestMessage() {
        return "Hello there";
    }
}
