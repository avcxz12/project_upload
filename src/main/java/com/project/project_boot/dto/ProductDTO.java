package com.project.project_boot.dto;

import lombok.Data;

@Data
public class ProductDTO {

	private String title;
	private int price;
	private int EA;
	private String mainmenu;
	private String submenu;
	private String userid;
	private String img;
	
	
	public static ProductDTO MakeDto(String title, int price, int EA, String mainmenu, String submenu, String savename,String userid) {
		ProductDTO productDTO =new ProductDTO();
		
		productDTO.setTitle(title);
		productDTO.setPrice(price);
		productDTO.setEA(EA);
		productDTO.setMainmenu(mainmenu);
		productDTO.setSubmenu(submenu);
		productDTO.setImg(savename);
		productDTO.setUserid(userid);
		
		
		
		return productDTO;
	}
	
}