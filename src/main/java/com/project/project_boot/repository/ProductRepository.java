package com.project.project_boot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.project_boot.dto.ProductDTO;
import com.project.project_boot.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	
Page<Product> findAllBymainmenuContains(String mainmenu, Pageable pageable);
	Page<Product> findAllBytitleContains(String search_name, Pageable pageable);

	List<Product> findAllByimg(String img);

	Page<Product> findAllBysubmenuContains(String submenu, Pageable pageable);
	Page<Product> findAllByTitleContainsOrderByPriceDesc(String search,Pageable pageable);
	Page<Product> findAllByTitleContainsOrderByPriceAsc(String search,Pageable pageable);


}