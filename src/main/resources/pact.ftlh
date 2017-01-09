import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("test")
public class UserClientTest {
    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("${provider}", "localhost", 8085, this);

    @Autowired
    private ${clientClass} client;

    @Test
    @PactVerification
    public void getUserPacks() throws Exception {
        // service call
        client.${methodName}();
    }

    @Pact(consumer = "${consumer}")
    public PactFragment createFragment(PactDslWithProvider builder) {

        // implement the response body message
        String response = "";

        return builder
                .uponReceiving("some description")
                .path("${path}")
                .method("${httpMethod}")
                .willRespondWith()
                .status(200)
                .body(response)
                .toFragment();
    }
}