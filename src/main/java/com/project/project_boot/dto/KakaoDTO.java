package com.project.project_boot.dto;


import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class KakaoDTO {

	private String userid;
	private String title;
	private int price;
	private int ea;
	private String img;
	@Temporal(TemporalType.DATE)
	  @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date daytime;
	private String tid;
	
	public KakaoDTO makeDto(String id ,String title,int price,int ea,String img,Date date,String tid) {
		KakaoDTO kakaoDTO = new KakaoDTO();
		
		kakaoDTO.setUserid(id);
		kakaoDTO.setTitle(title);
		kakaoDTO.setPrice(price);
		kakaoDTO.setEa(ea);
		kakaoDTO.setImg(img);
		kakaoDTO.setDaytime(date);
		kakaoDTO.setTid(tid);
		return kakaoDTO;
	}
}