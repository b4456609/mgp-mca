package soselab.easylearn.MCA.parser;

import org.json.JSONObject;
import soselab.easylearn.MCA.parser.model.ServiceEndpoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bernie on 1/9/17.
 */
public class EndpointParser {

    private final String serviceName;
    private final String packageName;

    public EndpointParser(String serviceName, String packageName) {
        this.serviceName = serviceName;
        this.packageName = packageName;
    }

    public List<ServiceEndpoint> getMappings(String mappingJson){
        JSONObject jsonObject = new JSONObject(mappingJson);
        List<ServiceEndpoint> pathMedthodPair = new ArrayList<>();
        Iterator keys = jsonObject.keys();
        while(keys.hasNext()){
            String key = (String) keys.next();
            JSONObject target = jsonObject.getJSONObject(key);
            //endpoint filter
            if(target.has("method") && target.getString("method").contains(packageName)){

                String path = getPath(key);
                String httpMethod = getHttpMethod(key);
                String id = String.format("%s endpoint %s %s", serviceName, path, httpMethod);

                pathMedthodPair.add(new ServiceEndpoint(id,path,httpMethod,target.getString("method")));
            }
        }
        return pathMedthodPair;
    }

    private String getPath(String input){

        Pattern pattern = Pattern.compile("\\[.*?\\]");
        Matcher matcher = pattern.matcher(input);
        matcher.find();
        String group = matcher.group();
        String path = group.substring(1, group.length() - 1);
        return path;
    }


    private String getHttpMethod(String input){

        Pattern pattern = Pattern.compile("=\\[.*?\\]");
        Matcher matcher = pattern.matcher(input);
        matcher.find();
        String group = matcher.group();
        String method = group.substring(2, group.length() - 1);
        return method;
    }
}
