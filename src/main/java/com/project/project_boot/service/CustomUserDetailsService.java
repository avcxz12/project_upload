package com.project.project_boot.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.project.project_boot.entity.Member;
import com.project.project_boot.repository.MemberRepository;
import com.project.project_boot.security.SecurityUser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    	Optional<Member> findMember = memberRepository.findById(id);
        if (!findMember.isPresent()) throw new UsernameNotFoundException("존재하지 않는 아이디 입니다.");


        return new SecurityUser(findMember.get());
    }
}