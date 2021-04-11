package com.idea2software.acoustic.blog.search.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.idea2software.acoustic.blog.search.api.entity.Author;
import com.idea2software.acoustic.blog.search.api.entity.Blog;
import com.idea2software.acoustic.blog.search.api.entity.BlogContent;
import com.idea2software.acoustic.blog.search.api.repository.AuthorRepository;
import com.idea2software.acoustic.blog.search.api.repository.BlogContentRepository;
import com.idea2software.acoustic.blog.search.api.repository.BlogRepository;

@Service
public class BlogSearchService {
	
	private static final int PAGE_SIZE = 10;

	@Autowired
	private BlogContentRepository blogContentRepository;

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private AuthorRepository authorRepository;

	public List<BlogContent> getAutoFillContent(String query) {
		return blogContentRepository.findTop10ByContentContainingIgnoreCase(query);
	}

	public List<BlogContent> getBlogsByPage(String query, int page) {
		return blogContentRepository.findTop10ByContentContainingIgnoreCase(query, PageRequest.of(page, PAGE_SIZE));
	}

	public Blog getBlogById(int blogId) {
		return blogRepository.findById(blogId).get();
	}

	public Author getAuthorById(int authorId) {
		return authorRepository.findById(authorId).get();
	}

	public long getTotalPages(String query) {
		long count = blogContentRepository.countByContentContainingIgnoreCase(query);
		return Double.valueOf(Math.ceil((double)count / PAGE_SIZE)).longValue();
	}

}
