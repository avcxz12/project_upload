package com.project.project_boot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.project_boot.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>{

	List<Inquiry> findByUserid(String userid);

	List<Inquiry> findByTypeAndUserid(String cancel,String userid);
	
	List<Inquiry> findAllByInquiryseq(Long seq_num);

	List<Inquiry> findAllByType(String apply);

	List<Inquiry> findAllByTypeNot(String apply);
	

}
