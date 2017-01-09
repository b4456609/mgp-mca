package soselab.easylearn.MCA.parser;

import soselab.easylearn.MCA.parser.model.ClientCaller;
import soselab.easylearn.MCA.parser.model.ServiceEndpoint;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by bernie on 1/9/17.
 */
public class ClientEndpointMapping {
    public Map<ClientCaller, Set<ServiceEndpoint>> clientEndpointDep(List<ClientCaller> client, List<ServiceEndpoint> serviceEndpoints) {
        Map<ClientCaller, Set<ServiceEndpoint>> clientCallerSetMap = new HashMap<>();
        for (ClientCaller clientCaller : client) {
            Set<Member> clientCallerMembers = clientCaller.getMembers();
            clientCallerMembers.forEach(clientCallerMember -> {
                serviceEndpoints.forEach(serviceEndpoint -> {
                    String serviceEndpointMethod = serviceEndpoint.getMethod();
                    int firstSpace = serviceEndpointMethod.indexOf(" ");
                    int sechondSpace = serviceEndpointMethod.indexOf(" ", firstSpace + 1);
                    String method = serviceEndpointMethod.substring(sechondSpace);


                    String callerMethodStr = ((Method) clientCallerMember).toString();
                    int callerFirstSpace = callerMethodStr.indexOf(" ");
                    int callerSechondSpace = callerMethodStr.indexOf(" ", callerFirstSpace + 1);
                    String callerMethod = callerMethodStr.substring(callerSechondSpace);


                    if (method.equals(callerMethod)) {
                        if (clientCallerSetMap.containsKey(clientCaller)) {
                            clientCallerSetMap.get(clientCaller).add(serviceEndpoint);
                        } else {
                            clientCallerSetMap.put(clientCaller,
                                    new HashSet<ServiceEndpoint>() {{
                                        add(serviceEndpoint);
                                    }});
                        }
                    }
                });
            });

        }
        return clientCallerSetMap;
    }
}
