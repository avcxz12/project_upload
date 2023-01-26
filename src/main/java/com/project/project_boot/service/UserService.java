package com.project.project_boot.service;


import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.project_boot.constant.Role;
import com.project.project_boot.entity.Member;
import com.project.project_boot.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@DynamicUpdate
public class UserService {
	private final MemberRepository memberRepository;
	
	
	public Member saveMember(Member member)
	{
		validateDuplicateUser(member);
		return memberRepository.save(member);
	}
	
	private void validateDuplicateUser(Member member)
	{
		Member findMember = memberRepository.findAllById(member.getId());
		if(findMember !=null)
		{
			throw new IllegalStateException("중복된 아이디 입니다.");
		}
		
	}
	
	public String findingUserName(String userid)
	{
		String name="";
		Member findUsername = memberRepository.findAllById(userid);
		name = findUsername.getName();
		
		return name;
	}
	public Member usermember(String userid){
		Member mem=memberRepository.findAllById(userid);
		System.out.println(mem.getUser_num());
		mem.setUser_num(mem.getUser_num());
		mem.setName(mem.getName());
		mem.setId(mem.getId());
		mem.setPw(mem.getPw());
		mem.setPhone(mem.getPhone());
		mem.setAddress(mem.getAddress());
		mem.setRole(Role.MANAGER);
		System.out.println("성공?");
		return memberRepository.save(mem);
	}
	
	public Member modifyMember(Member member)
	{
		Member findMember = memberRepository.findAllById(member.getId());
		if(findMember ==null)
		{
			throw new IllegalStateException("회원찾기 실패");
		}
		findMember.setId(member.getId());
		findMember.setName(member.getName());
		findMember.setPw(member.getPw());
		findMember.setPhone(member.getPhone());
		findMember.setAddress(member.getAddress());
		System.out.println("정보변경");
		return memberRepository.save(findMember);
	}

	public String findingUserpass(String name) {
		
		Member member = new Member();
		
		member = memberRepository.findAllById(name);
		String pw = member.getPw();
		return pw;
	}
	
	
	
}
