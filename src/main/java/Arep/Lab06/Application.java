package Arep.Lab06;

import Arep.Lab06.controller.MathController;

import java.io.IOException;
import java.net.URISyntaxException;

public class Application {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // Configure static files folder (Lab 05 feature)
        HttpServer.staticfiles("/webroot/public");

        // Use reflection to register all @GetMapping routes from the controller
        MicroSpringBoot.run(MathController.class, args);
    }
}
