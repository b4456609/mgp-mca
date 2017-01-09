package soselab.easylearn.MCA.factory;

/**
 * Created by bernie on 1/9/17.
 */
public class IDFactory {

    public String getEndpointId(String serviceName, String path, String httpMethod) {
        return String.format("%s endpoint %s %s", serviceName, path, httpMethod);
    }

    public String getServiceCallerId(String target, String path, String httpMethod) {
        return String.format("%s serviceCall %s %s", target, path, httpMethod);
    }

}
