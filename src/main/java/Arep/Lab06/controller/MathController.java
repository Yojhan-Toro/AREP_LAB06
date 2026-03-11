package Arep.Lab06.controller;

import Arep.Lab06.HttpRequest;
import Arep.Lab06.HttpResponse;
import Arep.Lab06.annotations.GetMapping;
import Arep.Lab06.annotations.RequestParam;
import Arep.Lab06.annotations.RestController;

@RestController
public class MathController {

    @GetMapping("/App/hello")
    public static String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name + "!";
    }

    @GetMapping("/App/pi")
    public static String pi() {
        return "PI = " + Math.PI;
    }

    @GetMapping("/App/e")
    public static String euler() {
        return "e = " + Math.E;
    }

}
