package org.fxee.test.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ModuleController {

    @GetMapping(path = "/message")
    public String printModuleTestMessage() {
        return "Hello there";
    }
}
