package com.tibil.assessment.gateway.factory;

import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("flattenJsonParser")
public class FlattenJsonParser implements JsonParserStrategy{

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public String parse(String jsonResponse) {
		try {
			JsonNode root = objectMapper.readTree(jsonResponse);
			StringBuilder result = new StringBuilder();
			if(root.isObject()) {
				Iterator<Map.Entry<String, JsonNode>> fields = root.fields();
				while(fields.hasNext()) {
					Map.Entry<String, JsonNode> entry = fields.next();
					result.append(entry.getKey()).append(": ").append(entry.getValue().asText()).append(", ");
				}
			}
			return result.toString();
		} catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
	
	}

	
}
