package soselab.easylearn.MCA.parser.model;

import java.lang.reflect.Method;

/**
 * Created by bernie on 1/9/17.
 */
public class ClientInfo {
    private final Method method;
    private final String path;
    private final String httpMethod;
    private final String target;

    public ClientInfo(Method method, String path, String httpMethod, String target) {
        this.method = method;
        this.path = path;
        this.httpMethod = httpMethod;
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
