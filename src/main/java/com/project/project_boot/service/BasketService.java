package com.project.project_boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.project_boot.entity.Basket;
import com.project.project_boot.entity.Product;
import com.project.project_boot.repository.BasketRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Repository
public class BasketService {

	@Autowired
	private final BasketRepository basketRepository;
	
	
	public Basket BasketImport(Basket bakset) {
		
		validdataDuplicateProduct(bakset);
		
	return basketRepository.save(bakset);
	}


	private void validdataDuplicateProduct(Basket bakset) {
		
		
	}
	public List<Basket> BasketTest(String title,String buyer) {
        List<Basket>blist=basketRepository.findByTitleAndBuyer(title,buyer);
		return blist;
		}
	public void BasketdeleteTest(String title,String buyer) {
		 basketRepository.deleteBytitleAndBuyer(title,buyer);
	 }

	public List<Basket> BasketList(String buyer) {
        List<Basket>blist=basketRepository.findAllBybuyer(buyer);
		return blist;
		}
	
	
	 






		
}
