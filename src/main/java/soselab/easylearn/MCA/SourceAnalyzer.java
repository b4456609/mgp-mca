package soselab.easylearn.MCA;

import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soselab.easylearn.MCA.parser.ClientEndpointParser;
import soselab.easylearn.MCA.parser.ClientParser;
import soselab.easylearn.MCA.parser.model.ClientCaller;
import soselab.easylearn.MCA.parser.model.ClientInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Created by bernie on 1/6/17.
 */

public class SourceAnalyzer {


    private static final Logger LOGGER = LoggerFactory.getLogger(SourceAnalyzer.class);
    Reflections srcReflections;
    Reflections testReflections;


    public SourceAnalyzer(Collection<URL> allLocation) {
        allLocation.forEach(location -> {
            if (location.getPath().contains("main")) {
                srcReflections = new Reflections(new ConfigurationBuilder()
                        .setUrls(location)
                        .filterInputsBy(new FilterBuilder().exclude("test"))
                        .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(), new FieldAnnotationsScanner(),
                                new MethodAnnotationsScanner(), new MethodParameterScanner(), new MethodParameterNamesScanner(),
                                new MemberUsageScanner()));
            } else if (location.getPath().contains("test")) {
                testReflections = new Reflections(new ConfigurationBuilder()
                        .setUrls(allLocation)
                        .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(), new FieldAnnotationsScanner(),
                                new MethodAnnotationsScanner(), new MethodParameterScanner(), new MethodParameterNamesScanner(),
                                new MemberUsageScanner()));
            }
        });
    }

    public List<ClientCaller> getClient() {
        ClientParser clientParser = new ClientParser(srcReflections);
        ClientEndpointParser clientEndpointParser = new ClientEndpointParser(srcReflections);

        List<ClientInfo> client = clientParser.getClient();
        List<ClientCaller> info = clientEndpointParser.getInfo(client);
        return info;
    }

    public Set<ClientCaller> getUnTestClient(List<ClientCaller> clientCallers) {
        Set<ClientCaller> unTestClientCaller = new HashSet<>();
        clientCallers.forEach(clientCaller -> {
            Set<Member> methodUsage = testReflections.getMethodUsage(clientCaller.getClientInfo().getMethod());
            boolean isTest = false;
            for (Member member : methodUsage) {
                Annotation[] declaredAnnotations = ((Method) member).getDeclaredAnnotations();
                List<Annotation> list = Arrays.asList(declaredAnnotations);
                long count = list.stream()
                        .filter(annotation -> annotation.toString().contains("PactVerification"))
                        .count();
                if (count != 0) {
                    isTest = true;
                    break;
                }
            }
            if (!isTest) {
                unTestClientCaller.add(clientCaller);
            }
        });
        return unTestClientCaller;
    }

}
