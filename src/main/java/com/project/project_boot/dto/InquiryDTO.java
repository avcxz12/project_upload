package com.project.project_boot.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InquiryDTO {

	private Long inquiry_SEQ;
	private String userid;
	private String title;
	private String content;
	private String type;
	private Date inquiry_date;
	private String answer;
	
	public static InquiryDTO UpdateInquiryDTO(int inq_num,String userid,String title,String content, String type,String Answer)
	{
		Date date = new Date();
		InquiryDTO inquiryDTO = new InquiryDTO();
		Long inquiry_num=Long.valueOf(inq_num);	
		inquiryDTO.setInquiry_SEQ(inquiry_num);
		inquiryDTO.setUserid(userid);
		inquiryDTO.setTitle(title);
		inquiryDTO.setContent(content);
		inquiryDTO.setType(type);
		inquiryDTO.setInquiry_date(date);
		inquiryDTO.setAnswer(Answer);
		return inquiryDTO;
	}
	public static InquiryDTO createInquiryDTO(String userid,String title,String content, String type)
	{
		Date date = new Date();
		InquiryDTO inquiryDTO = new InquiryDTO();
		inquiryDTO.setUserid(userid);
		inquiryDTO.setTitle(title);
		inquiryDTO.setContent(content);
		inquiryDTO.setType(type);
		inquiryDTO.setInquiry_date(date);
		 return inquiryDTO;
	}
	
}
