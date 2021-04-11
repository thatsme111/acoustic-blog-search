package com.idea2software.acoustic.blog.search.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.idea2software.acoustic.blog.search.api.entity.Blog;

@Repository
public interface BlogRepository extends CrudRepository<Blog, Integer> {

	
	
}
