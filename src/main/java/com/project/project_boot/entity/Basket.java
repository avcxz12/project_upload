package com.project.project_boot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.project.project_boot.dto.BasketDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="basket")
@Setter @Getter
@ToString
public class Basket {
	@Id
	@Column(name ="basket_num")
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "basket_num_seq")
	@SequenceGenerator(name ="basket_num_seq",sequenceName="basketnum_SEQ",allocationSize = 1)
	private Long basket_SEQ;
	private String userid;
	private String title;
	private int price;
	private String mainmenu;
	private String img;
	private int EA;
	private String buyer;
	public static Basket createBasket(BasketDTO basketDTO)
{
	 Basket basket = new Basket();
	 basket.setUserid(basketDTO.getUserid());
	 basket.setTitle(basketDTO.getTitle());
	 basket.setPrice(basketDTO.getPrice());
	 basket.setMainmenu(basketDTO.getMainmenu());
	 basket.setImg(basketDTO.getImg());
	 basket.setEA(basketDTO.getEA());
	 basket.setBuyer(basketDTO.getBuyer());
	 return basket;
}
}


