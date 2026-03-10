package Arep.Lab06;
import Arep.Lab06.annotations.GetMapping;
import Arep.Lab06.annotations.RestController;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

public class MicroSpringBoot {


    public static void run(Class<?> controllerClass, String[] args)
            throws IOException, URISyntaxException {

        System.out.println("Loading components from: " + controllerClass.getName());

        if (!controllerClass.isAnnotationPresent(RestController.class)) {
            System.err.println("Class " + controllerClass.getName()
                    + " is not annotated with @RestController. Aborting.");
            return;
        }

        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping mapping = method.getAnnotation(GetMapping.class);
                String path = mapping.value();

                HttpServer.get(path, (req, res) -> {
                    try {
                        Class<?>[] paramTypes = method.getParameterTypes();
                        Object result;

                        if (paramTypes.length == 2
                                && paramTypes[0] == HttpRequest.class
                                && paramTypes[1] == HttpResponse.class) {
                            result = method.invoke(null, req, res);
                        } else if (paramTypes.length == 1
                                && paramTypes[0] == HttpRequest.class) {
                            result = method.invoke(null, req);
                        } else {
                            // No-arg method (like Lab 06's HelloController)
                            result = method.invoke(null);
                        }

                        return result != null ? result.toString() : "";

                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Error invoking method: " + e.getMessage();
                    }
                });

                System.out.println("Registered route: GET " + path
                        + " -> " + controllerClass.getSimpleName() + "." + method.getName() + "()");
            }
        }

        System.out.println("All routes registered. Starting HttpServer on port 35000...");
        HttpServer.main(args);
    }
}
