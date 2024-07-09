package com.penview.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penview.security.entity.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, Long>{

	UserEntity findByUsername(String username);
}
