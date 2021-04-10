package com.idea2software.acoustic.blog.search.api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.idea2software.acoustic.blog.search.api.entity.BlogContent;

public interface BlogContentRepository extends CrudRepository<BlogContent, Integer> {

	public List<BlogContent> findTop10ByContentContainingIgnoreCase(String query);
	
}
