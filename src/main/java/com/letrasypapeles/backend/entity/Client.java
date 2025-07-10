package com.letrasypapeles.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Entity
@Table(name="tbl_clients")
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true)
	private String email;

	private String password;

	private Integer fidelityPoints;

	// @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	// @JoinTable(
	// 	name = "tbl_client_roles",
	// 	joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"),
	// 	inverseJoinColumns = @JoinColumn(name = "role_name", referencedColumnName = "rolName")
	// )

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class, cascade = CascadeType.PERSIST)
	@JoinTable(
		name = "tbl_user_roles",
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	)
	private Set<Role> roles;



}
