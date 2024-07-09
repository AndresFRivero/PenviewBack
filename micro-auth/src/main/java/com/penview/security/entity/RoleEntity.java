package com.penview.security.entity;

import java.util.HashSet;
import java.util.Set;

import com.penview.security.util.RoleEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbRoles")
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long consecutivoRole;
	
	@Column(name = "name")
	@Enumerated(EnumType.STRING)
	private RoleEnum roleEnum;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "tbRolesPermissions", joinColumns = @JoinColumn(name = "consecutivoRole"), inverseJoinColumns = @JoinColumn(name = "consecutivoPermission"))
	private Set<PermissionEntity> permissions = new HashSet<>();
}
