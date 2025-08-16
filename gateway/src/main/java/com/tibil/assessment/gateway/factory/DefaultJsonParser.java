package com.tibil.assessment.gateway.factory;

import org.springframework.stereotype.Component;

@Component("defaultJsonParser")
public class DefaultJsonParser implements JsonParserStrategy{

	@Override
	public String parse(String jsonResponse) {
		// For now, returns raw JSON, but can be extended
		return jsonResponse;
	}

	
}
