package com.project.project_boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.project_boot.entity.KakaoResult;
import com.project.project_boot.repository.KakaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Repository
public class KakaoService {
	
	@Autowired
	private final KakaoRepository kakaoRepository;

	public void saveResult(KakaoResult kakaoresult) {
		
		kakaoRepository.save(kakaoresult);
		
	}

	public List<KakaoResult> getPayResult(String userid) {
		
		return kakaoRepository.findAllByuseridOrderByDaytimeDesc(userid);
		
		

	}

	public void paycancel(String tid) {
		
		kakaoRepository.deleteBytid(tid);
		
	}
	//구매한 유저들 정보
	public List<KakaoResult> finduser(String userid, String img) {
		List<KakaoResult> kList=kakaoRepository.findAllByUseridAndImg(userid,img);
		return kList;
	}
	
	public List<KakaoResult> payUser(String userid) {
		List<KakaoResult> kList=kakaoRepository.findAllByUserid(userid);
		return kList;
	}
	
	public KakaoResult getproduct_detail(String tid)
	{
		return kakaoRepository.findAllBytid(tid);
	}
	
	
	
}
