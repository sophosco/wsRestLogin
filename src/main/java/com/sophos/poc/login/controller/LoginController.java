package com.sophos.poc.login.controller;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sophos.poc.login.model.LoginResponse;
import com.sophos.poc.login.model.Status;
import com.sophos.poc.login.model.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LoginController {
	
	
	private static final Logger logger = LogManager.getLogger(LoginController.class);
	private static ObjectMapper mapper = new ObjectMapper();

	
	@RequestMapping(value = "/api/users/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Status> login(
			@RequestHeader(value = "X-RqUID", required = true) String xRqUID,
			@RequestHeader(value = "X-Channel", required = true) String xChannel,
			@RequestHeader(value = "X-IPAddr", required = true) String xIPAddr,
			@RequestHeader(value = "X-Sesion", required = true) String xSesion, 
			@RequestHeader(value = "X-HaveToken", required = false, defaultValue = "true" ) boolean xHaveToken, 
			@RequestBody Users users) throws JsonProcessingException 
	{
		try {
			logger.info("Headers: xSesion["+ xSesion +"] ");
			logger.info("Request: "+mapper.writeValueAsString(users));
			String defaultError ="ERROR Ocurrio una exception inesperada";

			if((xSesion == null || xSesion.isEmpty()) || (xHaveToken)) {
				Status status = new Status("500","El token no es valido o ya expiro. Intente mas tarde", defaultError, null);
				ResponseEntity<Status> res = new ResponseEntity<>(status, HttpStatus.UNAUTHORIZED);
				logger.info("Response ["+ res.getStatusCode() +"] :"+mapper.writeValueAsString(res));
				return res;
			}
			if(users == null) {
				Status status = new Status("500", defaultError, "Objecto User es <NULL>", null);
				ResponseEntity<Status> res = new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
				logger.info("Response ["+ res.getStatusCode() +"] :"+mapper.writeValueAsString(res));
				return res;
			}
			
			if(xRqUID == null || xChannel == null || xIPAddr == null ) {
				Status status = new Status("500", defaultError, "Valor <NULL> en alguna cabecera obligatorio (X-RqUID X-Channel X-IPAddr X-Sesion)", null);
				ResponseEntity<Status> res = new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
				logger.info("Response ["+ res.getStatusCode() +"] :"+mapper.writeValueAsString(res));
				return res;
			}
			
			if(users.getIdSesion() == null || users.getIdSesion().isEmpty())
				users.setIdSesion(UUID.randomUUID().toString());
			
				LoginResponse response = new LoginResponse(System.currentTimeMillis()+"".lastIndexOf(4)+"");
				Status status = new Status("0", "Operacion Exitosa", "", response);
				ResponseEntity<Status> res = new ResponseEntity<>(status, HttpStatus.OK);
				logger.info("Response ["+ res.getStatusCode() +"] :"+mapper.writeValueAsString(res));
				return res;


		} catch (Exception e) {
			logger.error("Ocurrio un excepcion inesperada",e);
			e.printStackTrace();
			Status status = new Status("500", "ERROR Ocurrio una exception inesperada", e.getMessage(), null);
			ResponseEntity<Status> res = new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
			logger.info("Response ["+ res.getStatusCode() +"] :"+mapper.writeValueAsString(status));
			return res;
		}
	}
	

}
