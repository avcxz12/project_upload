package com.project.project_boot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.project_boot.entity.Inquiry;
import com.project.project_boot.repository.InquiryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Repository
public class InquiryService {

	@Autowired
	private final InquiryRepository inquiryRepository;

	public Inquiry InquiryImport(Inquiry inquiry) {

		validdataDuplicateProduct(inquiry);
		
	return inquiryRepository.save(inquiry);
	}


	private void validdataDuplicateProduct(Inquiry inquiry) {
		
		
	}


	public List<Inquiry> findUserid(String userid) {
	
		List<Inquiry>InList=inquiryRepository.findByUserid(userid);
		return InList;
		
	}
	public List<Inquiry> check(String check,String userid) {
		List<Inquiry>InList=new ArrayList<>();
		if(check.equals("1")) {
			InList=inquiryRepository.findByUserid(userid);
		}
		else if(check.equals("주문취소/환불")||check.equals("기타 문의")) {
			InList=inquiryRepository.findByTypeAndUserid(check,userid);
		}
		return InList;
		
	}
	public List<Inquiry> findAdmin(String apply) {
		List<Inquiry>InList=inquiryRepository.findAllByTypeNot(apply);
		return InList;
	}


	public List<Inquiry> findUserpost(Long seq_num) {
		
		List<Inquiry> userpost = inquiryRepository.findAllByInquiryseq(seq_num);
		return userpost;
	}


	public List<Inquiry> seller_application(String apply) {
		List<Inquiry>InList=inquiryRepository.findAllByType(apply);
		return InList;
	}


	
}
