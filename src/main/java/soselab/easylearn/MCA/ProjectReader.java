package soselab.easylearn.MCA;

import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soselab.easylearn.MCA.factory.IDFactory;
import soselab.easylearn.MCA.gen.TestingCodeGen;
import soselab.easylearn.MCA.output.FileOutput;
import soselab.easylearn.MCA.output.MDPWriter;
import soselab.easylearn.MCA.output.model.*;
import soselab.easylearn.MCA.parser.ClientEndpointMapping;
import soselab.easylearn.MCA.parser.EndpointParser;
import soselab.easylearn.MCA.parser.model.ClientCaller;
import soselab.easylearn.MCA.parser.model.ClientInfo;
import soselab.easylearn.MCA.parser.model.ServiceEndpoint;

import java.io.FileReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by bernie on 1/9/17.
 */
public class ProjectReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectReader.class);

    private final String serviceName;
    private final SourceAnalyzer sourceAnalyzer;
    private final String mappingJson;
    private final EndpointParser endpointParser;
    private final ClientEndpointMapping clientEndpointMapping;
    private final IDFactory idFactory;
    private final String swaggerJson;
    private final TestingCodeGen testingCodeGen;
    private final FileOutput fileOutput;

    public ProjectReader(String serviceName, String mappingJson, String swaggerJson, String packageName) {
        this.serviceName = serviceName;
        // pass package name for analyzer
        this.sourceAnalyzer = new SourceAnalyzer(ClasspathHelper.forPackage(packageName));
        this.mappingJson = mappingJson;
        this.endpointParser = new EndpointParser(serviceName, packageName);
        this.clientEndpointMapping = new ClientEndpointMapping();
        this.idFactory = new IDFactory();
        this.swaggerJson = swaggerJson;
        testingCodeGen = new TestingCodeGen();
        fileOutput = new FileOutput();
    }

    public void execute(){
        List<ClientCaller> client = sourceAnalyzer.getClient();
        List<ServiceCall> serviceCalls = client.stream().map(clientCaller -> {
            ClientInfo clientInfo = clientCaller.getClientInfo();
            String id = idFactory.getServiceCallerId(clientInfo.getTarget(), clientInfo.getPath(), clientInfo.getHttpMethod());
            return new ServiceCall(id, clientInfo.getPath(), clientInfo.getHttpMethod(), clientInfo.getTarget());
        }).collect(Collectors.toList());

        List<ServiceEndpoint> serviceEndpoints = endpointParser.getMappings(mappingJson);
        List<Endpoint> endpoints = serviceEndpoints.stream().map(serviceEndpoint -> {
            return new Endpoint(serviceEndpoint.getId(), serviceEndpoint.getPath(), serviceEndpoint.getHttpMethod());
        }).collect(Collectors.toList());


        List<EndpointDep> endpointDeps = new ArrayList<>();
        Map<ClientCaller, Set<ServiceEndpoint>> clientCallerSetMap = clientEndpointMapping.clientEndpointDep(client, serviceEndpoints);
        clientCallerSetMap.forEach((clientCaller, serviceEndpoints1) -> {
            ClientInfo clientInfo = clientCaller.getClientInfo();
            String serviceCallerId = idFactory.getServiceCallerId(clientInfo.getTarget(), clientInfo.getPath(), clientInfo.getHttpMethod());
            serviceEndpoints1.forEach(serviceEndpoint -> {
                endpointDeps.add(new EndpointDep(serviceEndpoint.getId(), serviceCallerId));
            });
        });

        //generate testing code
        Set<ClientCaller> unTestClients = sourceAnalyzer.getUnTestClient(client);
        LOGGER.info("untestmethod information" + unTestClients.toString());

        unTestClients.forEach(unTestClient -> {
            ClientInfo clientInfo = unTestClient.getClientInfo();

            String codeSnippet = testingCodeGen.genCode(clientInfo.getTarget(), serviceName,
                    clientInfo.getMethod().getName(), clientInfo.getPath(),
                    clientInfo.getHttpMethod(), clientInfo.getMethod().getDeclaringClass().getName());
            System.out.println(codeSnippet);
            fileOutput.writeFile(clientInfo.getMethod().getName() + ".txt", codeSnippet);
            LOGGER.info(codeSnippet);
        });

        //MDP write
        MDP mdp = new MDPBuilder()
                .setName(serviceName)
                .setEndpointDep(endpointDeps)
                .setServiceCall(serviceCalls)
                .setEndpoint(endpoints)
                .setSwagger(swaggerJson)
                .createMDP();

        MDPWriter mdpWriter = new MDPWriter();
        String mdpJson = mdpWriter.write(mdp);

        fileOutput.writeFile("mpd.json", mdpJson);

    }
}
