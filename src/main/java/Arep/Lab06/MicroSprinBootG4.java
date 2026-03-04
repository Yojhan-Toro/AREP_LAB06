package Arep.Lab06;

import org.springframework.boot.SpringApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MicroSprinBootG4 {

    static Map<String , Method> controllerMethod = new HashMap();

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        System.out.println("Loading commpomments ...");

        Class c = Class.forName(args[0]);


        if(c.isAnnotationPresent(RestController.class)){
            for(Method m c.getDeclaredMethods()){
                if(m.isAnnotationPresent(GetMapping.class)){
                    GetMapping a = m.getAnnotation(GetMapping.class);
                            String  path = a.value();
                }

            }
        }

        System.out.println("Invoking method for paht: "+ args[1]);

        Method m = controllerMethod.get(args[1]);
        System.out.println(m.invoke(null));


    }

}

