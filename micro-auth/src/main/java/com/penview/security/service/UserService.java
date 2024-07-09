package com.penview.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.penview.security.entity.UserEntity;
import com.penview.security.repository.IUserRepository;
import java.util.List;

@Service
public class UserService {
	
	@Autowired
	private IUserRepository userRepository;
	
	public List<UserEntity> findAll() {
		return userRepository.findAll();
	}
	
	

}
