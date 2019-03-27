package com.sophos.poc.login.repository;


import org.springframework.data.repository.CrudRepository;

import com.sophos.poc.login.model.Users;

public interface UserRepository extends CrudRepository<Users, String>{
	
}
