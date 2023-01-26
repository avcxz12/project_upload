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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="coment")
@Setter @Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Content {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_num_SEQ")
	@SequenceGenerator(name ="content_num_SEQ",sequenceName="content_SEQ",allocationSize = 1)
	private Long coment_num;
	
	private String userid;
	private String answer;
	private String img;
	@Temporal(TemporalType.DATE)
	  @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date daytime;
	private int rating;
	
	public Content makecontent(String userid,String answer,String img,Date date,int rating) {
		
		Content content = new Content();
		content.setUserid(userid);
		content.setAnswer(answer);
		content.setImg(img);
		content.setDaytime(date);
		content.setRating(rating);
		
		return content;
	}
}
