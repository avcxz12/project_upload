package com.project.project_boot.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.project.project_boot.dto.InquiryDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="inquirylist")
@Setter @Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Inquiry {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inquiry_num_seq")
	@SequenceGenerator(name ="inquiry_num_seq",sequenceName="inquirynum_seq",allocationSize = 1)
	private Long inquiryseq;
	
	private String userid;
	private String title;
	private String content;
	private String answer;
	private String type;
	@Temporal(TemporalType.DATE)
	  @DateTimeFormat(pattern = "yyyy-MM-dd")
	  private Date inquiry_date;
	
	public static Inquiry updateAnswer(InquiryDTO inquiryDTO)
	{
		Inquiry inquiry = new Inquiry();
		inquiry.setInquiryseq(inquiryDTO.getInquiry_SEQ());
		inquiry.setUserid(inquiryDTO.getUserid());
		inquiry.setTitle(inquiryDTO.getTitle());
		inquiry.setContent(inquiryDTO.getContent());
		inquiry.setType(inquiryDTO.getType());
		inquiry.setInquiry_date(inquiryDTO.getInquiry_date());
		inquiry.setAnswer(inquiryDTO.getAnswer());
		return inquiry;
	}
	public static Inquiry createinquiry(InquiryDTO inquiryDTO)
{
	Inquiry inquiry = new Inquiry();
	inquiry.setUserid(inquiryDTO.getUserid());
	inquiry.setTitle(inquiryDTO.getTitle());
	inquiry.setContent(inquiryDTO.getContent());
	inquiry.setType(inquiryDTO.getType());
	inquiry.setInquiry_date(inquiryDTO.getInquiry_date());
	 System.out.println("entity 에 저장된 값은 ="+inquiry);
	 return inquiry;
}
}


