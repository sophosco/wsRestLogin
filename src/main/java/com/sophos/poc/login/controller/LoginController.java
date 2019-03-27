package com.sophos.poc.login.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sophos.poc.login.controller.client.AuditClient;
import com.sophos.poc.login.controller.client.SecurityClient;
import com.sophos.poc.login.model.LoginResponse;
import com.sophos.poc.login.model.Status;
import com.sophos.poc.login.model.Users;
import com.sophos.poc.login.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LoginController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuditClient auditClient;
	
	@Autowired
	private SecurityClient securityClient;
	
	public LoginController(UserRepository userRepository, AuditClient auditClient, SecurityClient securityClient) {
		this.userRepository = userRepository;
		this.auditClient = auditClient;
		this.securityClient = securityClient;
	}
	
	@RequestMapping(value = "/api/users/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Status> login(
			@RequestHeader(value = "X-RqUID", required = true) String xRqUID,
			@RequestHeader(value = "X-Channel", required = true) String xChannel,
			@RequestHeader(value = "X-IPAddr", required = true) String xIPAddr,
			@RequestHeader(value = "X-Sesion", required = true) String xSesion, 
			@RequestHeader(value = "X-haveToken", required = false, defaultValue = "true" ) boolean xHaveToken, 
			@RequestBody Users users) 
	{
		try {
			
			if((xSesion == null || xSesion.isEmpty()) || (xHaveToken && HttpStatus.UNAUTHORIZED.equals(securityClient.verifyJwtToken(xSesion).getStatusCode()))) {
				Status status = new Status("500","El token no es valido o ya expiro. Intente mas tarde", "ERROR Ocurrio una exception inesperada", null);
				return new ResponseEntity<>(status, HttpStatus.UNAUTHORIZED);
			}
			if(users == null) {
				Status status = new Status("500", "ERROR Ocurrio una exception inesperada", "Objecto Orders es <NULL>", null);
				return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			if(xRqUID == null || xChannel == null || xIPAddr == null ) {
				Status status = new Status("500", "ERROR Ocurrio una exception inesperada", "Valor <NULL> en alguna cabecera obligatorio (X-RqUID X-Channel X-IPAddr X-Sesion)", null);
				return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			auditClient.saveAudit(
					xSesion,
					null,
					"Realizar Login",
					"Ingreso a la plataforma con Usuario",
					"Modulo de Login",
					null,
					null,
					users
			);
			
			Optional<Users> userCassandra = userRepository.findById(users.getEmail());
			
			
			if(userCassandra.isPresent() && userCassandra.get().getPassword().equals(users.getPassword())) {
				LoginResponse response = new LoginResponse(System.currentTimeMillis()+"".lastIndexOf(4)+"");
				Status status = new Status("0", "Operacion Exitosa", "", response);
				return new ResponseEntity<>(status, HttpStatus.OK);

			}else {
				LoginResponse response = new LoginResponse(System.currentTimeMillis()+"".lastIndexOf(4)+"");
				Status status = new Status("101", "ERROR en autenticacion de usuario. Usuario o Password Incorrecto.", "", response);
				return new ResponseEntity<>(status, HttpStatus.UNAUTHORIZED);
			}


		} catch (Exception e) {
			e.printStackTrace();
			Status status = new Status("500", "ERROR Ocurrio una exception inesperada", e.getMessage(), null);
			return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
