package com.project.project_boot.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.project.project_boot.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class SecurityUser extends User {
	
	private Member member;
	
	public SecurityUser(Member member) {
        super(member.getId(), member.getPw(), List.of(new SimpleGrantedAuthority("ROLE_"+member.getRole())));
        this.member = member;
    }

}