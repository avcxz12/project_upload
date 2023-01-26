package com.project.project_boot.dto;

import com.project.project_boot.entity.Basket;

import lombok.Data;

@Data
public class BasketDTO {

	private String userid;
	private String title;
	private int price;
	private String mainmenu;
	private String img;
	private int EA;
	private String buyer;
	
	public static BasketDTO createBasketDTO(String id , String title,int price,String mainmenu,String img,int ea,String buyer)
	{
		 BasketDTO basketDTO = new BasketDTO();
		 basketDTO.setUserid(id);
		 basketDTO.setTitle(title);
		 basketDTO.setPrice(price);
		 basketDTO.setMainmenu(mainmenu);
		 basketDTO.setImg(img);
		 basketDTO.setEA(ea);
		 basketDTO.setBuyer(buyer);
		 System.out.println("dto 에 저장된 ea 값은 ="+basketDTO.getEA());
		 return basketDTO;
	}
	
	
}
