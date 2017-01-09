package soselab.easylearn.MCA.parser;

import org.reflections.Reflections;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import soselab.easylearn.MCA.parser.model.ClientInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

public class ClientParser {

    private final Reflections reflections;

    public ClientParser(Reflections reflections) {
        this.reflections = reflections;
    }

    public List<ClientInfo> getClient(){
        ArrayList<Method> methods = new ArrayList<>();
        List<ClientInfo> collect = new ArrayList<>();

        System.out.println(reflections.getTypesAnnotatedWith(FeignClient.class));

        for(Class aClass: reflections.getTypesAnnotatedWith(FeignClient.class)){
            FeignClient mergedAnnotation = findMergedAnnotation(aClass, FeignClient.class);
            String target = mergedAnnotation.name();
            for(Method method: aClass.getDeclaredMethods()){
                RequestMapping methodMapping = findMergedAnnotation(method, RequestMapping.class);
                methods.add(method);
                collect.add(new ClientInfo(method, methodMapping.value()[0], methodMapping.method()[0].toString(), target));
            }
        }

        return collect;
    }
}
