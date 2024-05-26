package com.pbl.tasktoolintegration.jira.config;

import com.atlassian.adf.jackson2.AdfJackson2;
import com.atlassian.adf.model.node.Doc;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ADFDeserializer extends StdDeserializer<Doc> {
    protected ADFDeserializer() {
        this(null);
    }

    protected ADFDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Doc deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        AdfJackson2 adfJackson2 = new AdfJackson2();
        JsonNode jsonNode = p.getCodec().readTree(p);
        return adfJackson2.unmarshall(jsonNode.toString());
    }
}
