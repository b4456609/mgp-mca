package soselab.easylearn.MCA.gen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bernie on 1/9/17.
 */
public class TestingCodeGen {

    private final Configuration cfg;

    public TestingCodeGen() {
        cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        cfg.setLogTemplateExceptions(false);


    }

    public String genCode(String provider, String consumer, String methodName, String path, String httpMethod, String clientClass) {
        String str = "";
        try {
            Map root = new HashMap();
            root.put("provider", provider);
            root.put("consumer", consumer);
            root.put("methodName", methodName);
            root.put("path", path);
            root.put("httpMethod", httpMethod);
            root.put("clientClass", clientClass);

            Template temp = cfg.getTemplate("/pact.ftlh");

            /* Merge data-model with template */
            StringWriter out = new StringWriter();
            temp.process(root, out);
            str = out.getBuffer().toString();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            return str;
        }
    }
}
