package com.sophos.poc.login.controller.client;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sophos.poc.login.model.security.RequestHeader;
import com.sophos.poc.login.model.security.RequestPayload;
import com.sophos.poc.login.model.security.Status;
import com.sophos.poc.login.model.security.TokenRequest;

@Service
public class SecurityClient{

	public ResponseEntity<Status> verifyJwtToken(String jwt) throws JsonProcessingException{
		RestTemplate restTemplate = new RestTemplate(); 
		TokenRequest tokenRq = new TokenRequest();
		RequestHeader token = new RequestHeader();
		token.setToken(jwt);
		tokenRq.setRequestHeader(token);
		RequestPayload payload = new RequestPayload();
		payload.setId("1");
		tokenRq.setRequestPayload(payload);
		
		ResponseEntity<String> response = restTemplate.postForEntity(System.getenv("POC_SERVICE_SECURITY_VALIDATE"), tokenRq, String.class);
		JSONObject json = new JSONObject(response.getBody());
		String code = (String) json.getJSONObject("responseHeader").getJSONObject("status").get("code");
		
		if(code.equals("00") ) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	

}
