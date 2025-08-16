package com.tibil.assessment.gateway.factory;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JsonParserFactory {

	private final Map<String, JsonParserStrategy> parserMap;
    
    public JsonParserStrategy getParser(String url) {
    	// use flatten parser if URL contains "flatten"
    	if (url != null && url.contains("flatten")) {
            return parserMap.get("flattenJsonParser");
        }
    	//otherwise use default
        return parserMap.get("defaultJsonParser");
    }
}
