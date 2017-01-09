package soselab.easylearn.MCA.parser;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soselab.easylearn.MCA.SourceAnalyzer;
import soselab.easylearn.MCA.parser.model.ClientCaller;
import soselab.easylearn.MCA.parser.model.ClientInfo;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientEndpointParser {
    private final Reflections reflections;
    private static final Logger LOGGER = LoggerFactory.getLogger(SourceAnalyzer.class);


    public ClientEndpointParser(Reflections reflections) {
        this.reflections = reflections;
    }

    public List<ClientCaller> getInfo(List<ClientInfo> clientInfos){

        List<ClientCaller> clientCallers = new ArrayList<>();

        for(ClientInfo clientInfo : clientInfos){
            Set<Member> members = new HashSet<>();
            members.add(clientInfo.getMethod());
            members = getUsageMethod(members);

            clientCallers.add(new ClientCaller(clientInfo, members));
        }

        return clientCallers;
    }


    private Set<Member> getUsageMethod(Set<Member> methods) {
        LOGGER.debug("getUsageMethod"+methods.toString());
        Set<Member> methodUsages = new HashSet<>();
        for (Member member : methods) {
            Set<Member> methodUsage = reflections.getMethodUsage((Method) member);
            LOGGER.debug("getUsageMethod "+methodUsage.toString());
            methodUsages.addAll(methodUsage);
        }

        if (methodUsages.isEmpty()) {
            for (Member member : methods) {
                Method interfaceMember = getInterfaceMember((Method) member);
                if (interfaceMember != null) {
                    methodUsages.addAll(reflections.getMethodUsage(interfaceMember));
                    LOGGER.info("3"+methodUsages.toString());
                }
            }
        }

        if (methodUsages.isEmpty()) {
            return methods;
        }

        return getUsageMethod(methodUsages);
    }

    private Method getInterfaceMember(Method method) {
        for (Class c : method.getDeclaringClass().getInterfaces()) {
            LOGGER.info(c.toString() , c.getDeclaredMethods());
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(method.getName())) {
                    return m;
                }
            }
        }
        return null;
    }
}
