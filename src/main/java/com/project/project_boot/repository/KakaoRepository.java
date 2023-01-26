package com.project.project_boot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.project_boot.dto.KakaoDTO;
import com.project.project_boot.entity.KakaoResult;

public interface KakaoRepository extends JpaRepository<KakaoResult, Long>{

	List<KakaoResult> findAllByuserid(String userid);

	void deleteBytid(String tid);
	
	List<KakaoResult> findAllByUseridAndImg(String userid, String img);
	
	KakaoResult findAllBytid(String tid);

	List<KakaoResult> findAllByuseridOrderByDaytimeDesc(String userid);

	List<KakaoResult> findAllByUserid(String userid);

}
