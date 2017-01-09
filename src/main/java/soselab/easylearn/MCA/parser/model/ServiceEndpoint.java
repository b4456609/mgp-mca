package soselab.easylearn.MCA.parser.model;

/**
 * Created by bernie on 1/9/17.
 */
public class ServiceEndpoint {
    private final String id;
    private final String path;
    private final String httpMethod;
    private final String method;


    public ServiceEndpoint(String id, String path, String httpMethod, String method) {
        this.id = id;
        this.path = path;
        this.httpMethod = httpMethod;
        this.method = method;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "ServiceEndpoint{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
