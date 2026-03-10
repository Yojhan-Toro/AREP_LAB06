package Arep.Lab06;
@FunctionalInterface
public interface WebMethod {
    String execute(HttpRequest req, HttpResponse res);
}