package com.project.project_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.project_boot.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

	Optional<Member> findById(String id);
	
	Member findAllById(String id);

}
