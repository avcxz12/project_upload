package com.project.project_boot.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.project.project_boot.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name ="productinfo")
@Table(name ="productinfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	
	@Id
	private String title;
	private int price;
	private int EA;
	private String mainmenu;
	private String submenu;
	private String img;
	private String userid;
	
	
	public static Product MakeProductinfo(ProductDTO productDto)
	 {
		Product product = new Product();
		product.setTitle(productDto.getTitle());
		product.setPrice(productDto.getPrice());
		product.setEA(productDto.getEA());
		product.setMainmenu(productDto.getMainmenu());
		product.setSubmenu(productDto.getSubmenu());
		product.setImg(productDto.getImg());
		product.setUserid(productDto.getUserid());
			
		
		return product;
	 }

}