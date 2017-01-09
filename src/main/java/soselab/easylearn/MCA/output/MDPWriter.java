package soselab.easylearn.MCA.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import soselab.easylearn.MCA.output.model.MDP;

import java.io.*;

public class MDPWriter {
    public String write(MDP mdp) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
        //Object to JSON in String
        try {
            String jsonInString = mapper.writeValueAsString(mdp);

            return jsonInString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
