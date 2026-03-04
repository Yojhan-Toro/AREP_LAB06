package Arep.Lab06;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@RestController
public class HelloController {

    @GetMapping("/")
    public static String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/pi")
    public  static String pi(){
        return "3.14...";
    }

    @GetMapping("/hello")
    public  static String hello(){
        return "Hello ";
    }

}
