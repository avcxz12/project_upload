package com.project.project_boot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.project_boot.entity.Content;
import com.project.project_boot.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Repository
public class CommentService {
	
	private final CommentRepository commentRepository;
	
	public void savingcomment(Content content) {
		commentRepository.save(content);
	}

	public List<Content> viewComment(String img) {
		List<Content>clist=commentRepository.findAllByImg(img);
		return clist;
	}

	public int countrating(String img) {
		Long count=commentRepository.countByImg(img);
		int countInt = count.intValue();
		return countInt;
	}

}
