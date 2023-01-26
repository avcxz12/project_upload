package com.project.project_boot.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.project_boot.entity.Basket;

public interface BasketRepository extends JpaRepository<Basket, Long>{


	List<Basket> findAllBybuyer(String buyer);

	void deleteByTitleAndBuyer(String title, String buyer);

	List<Basket> findByTitleAndBuyer(String title,String buyer);

	void deleteBytitleAndBuyer(String title, String buyer);


}

