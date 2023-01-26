package com.project.project_boot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.project_boot.entity.Content;

public interface CommentRepository extends JpaRepository<Content, Long>{

	List<Content> findAllByImg(String img);

	Long countByImg(String img);

	
}
