package com.penview.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penview.security.entity.RoleEntity;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long>{

	List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);
}
