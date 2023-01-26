package com.project.project_boot.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserDTO{
	
	@NotEmpty(message = "이름은 필수 입력 값입니다.")
	private String name;
	@NotEmpty(message = "아이디는 필수 입력 값입니다.")
	private String id;
	@NotEmpty(message = "패스워드는 필수 입력 값입니다.")
	@Length(max=20 , min=12, message="비밀번호는 12~20자리 까지 입력 가능합니다.")
	private String pw;
	@NotEmpty(message = "휴대폰번호는 필수 입력 값입니다.")
	private String phone;
	@NotEmpty(message = "주소는 필수 입력 값입니다.")
	private String address;
	
	
}
