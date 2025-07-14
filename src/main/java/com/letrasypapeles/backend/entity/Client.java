package com.letrasypapeles.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Entity
@Table(name="tbl_clients")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
	// 	name = "tbl_user_roles",
	// 	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
	// 	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	// )
	// @Builder.Default
  // private Set<Role> roles = new HashSet<>();
    

	// public void addRole(Role role) {
	// 	if (role.getRoleName() == ERole.COMPRA) {
	// 		this.roles.add(role);
	// 	}
	// }

}




