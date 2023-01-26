package com.project.project_boot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.project_boot.dto.ProductDTO;
import com.project.project_boot.entity.Member;
import com.project.project_boot.entity.Product;
import com.project.project_boot.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Repository
public class ProductService {
	@Autowired
	private final ProductRepository productRepository;

	public Page<Product> productList(String search_name ,Pageable pageable) {
		Page<Product> plist = productRepository.findAllBytitleContains(search_name,pageable);
		return plist;
	}

	public Page<Product> productList_sub(String submenu,Pageable pageable) {
		
		Page<Product> plist = productRepository.findAllBysubmenuContains(submenu,pageable);
		return plist;
	}
	
	public Page<Product> productList_main(String mainmenu,Pageable pageable) {
		Page<Product> plist = productRepository.findAllBymainmenuContains(mainmenu,pageable);
		return plist;
	}
	public Page<Product> productList_filter(int num,String search,Pageable pageable) {
		if(num==1) {
		Page<Product> plist = productRepository.findAllByTitleContainsOrderByPriceDesc(search,pageable);
		
		return plist;
		}
			Page<Product> plist = productRepository.findAllByTitleContainsOrderByPriceAsc(search,pageable);
			
			return plist;
	}

	public Product product_registry(Product product, String filename) {

		String url = "/product_img/" + filename;
		System.out.println(url);
		product.setImg(url);

		return productRepository.save(product);
	}
	public Product product_easave(Product product) {
		
		return productRepository.save(product);
	}
	

	public List<Product> findtitleimg(String img) {
		List<Product> plist = productRepository.findAllByimg(img);
		return plist;
	}

}