package com.project.project_boot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.project_boot.constant.Role;
import com.project.project_boot.dto.UserDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="userinfo")
@Setter @Getter
@ToString
public class Member {
	
	
	@Id
	@Column(name="user_num")
	@GeneratedValue( strategy = GenerationType.SEQUENCE,generator="user_num_seq" )
	@SequenceGenerator(name = "user_num_seq", sequenceName = "userinfo_SEQ", allocationSize = 1)	
	private long user_num;
	
	private String name;
	
	private String id;
	
	private String pw;
	
	private String phone;
	
	private String address;
	
	 @Enumerated(EnumType.STRING)
	 private Role role;

	 public static Member createUser(UserDTO userDto,PasswordEncoder passwordEncoder)
	 {
		 Member user = new Member();
		 user.setName(userDto.getName());
		 user.setId(userDto.getId());
		 String password = passwordEncoder.encode(userDto.getPw());
		 user.setPw(password);
		 user.setPhone(userDto.getPhone());
		 user.setAddress(userDto.getAddress());
		 user.setRole(Role.USER);
		 return user;
	 }


}
