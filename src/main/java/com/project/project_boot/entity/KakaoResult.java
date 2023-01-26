package com.project.project_boot.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="payresult")
@Setter @Getter
public class KakaoResult {

	private String userid;
	
	@Id
	@Column(name = "tid")
	private String tid;
	
	private String title;
	
	private int price;
	
	private int eaa;
	
	private String img;
	@Temporal(TemporalType.DATE)
	  @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date daytime;
	
	

	public static KakaoResult makeresult(String name, String title, int price, int ea, String img, Date date,
			String tid) {
		KakaoResult kakaoResult = new KakaoResult();
		kakaoResult.setUserid(name);
		kakaoResult.setTid(tid);
		kakaoResult.setTitle(title);
		kakaoResult.setPrice(price);
		kakaoResult.setEaa(ea);
		kakaoResult.setImg(img);
		kakaoResult.setDaytime(date);
		
		return kakaoResult;
	}
	
}
