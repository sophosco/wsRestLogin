package com.sophos.poc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sophos.poc.login.controller.LoginController;
import com.sophos.poc.login.controller.client.AuditClient;
import com.sophos.poc.login.controller.client.SecurityClient;
import com.sophos.poc.login.model.Status;
import com.sophos.poc.login.model.Users;
import com.sophos.poc.login.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class LoginControllerTest {

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private SecurityClient securityClient;
	
	@Mock
	private AuditClient auditClient;

	@InjectMocks
	private LoginController controller;
	
	
	@Before
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
		MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void orderController_OK() throws Exception {
		when(securityClient.verifyJwtToken("Token"))
			.thenReturn(new ResponseEntity<>(HttpStatus.OK));
		
		ResponseEntity<Status> status = controller.login(
				UUID.randomUUID().toString(),
				"wsRestLogin",
				"localhost",
				UUID.randomUUID().toString(),
				false,
				new Users()
				);
		
		assertEquals(status.getBody().getCode(), "101");
	}
	
	
		

}
