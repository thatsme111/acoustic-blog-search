package com.idea2software.acoustic.blog.search.api;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idea2software.acoustic.blog.search.api.entity.Author;
import com.idea2software.acoustic.blog.search.api.entity.Blog;
import com.idea2software.acoustic.blog.search.api.entity.BlogContent;
import com.idea2software.acoustic.blog.search.api.service.BlogSearchService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@RestController
public class BlogSearchController {

	private static final Logger logger = LoggerFactory.getLogger(BlogSearchController.class);

	@Autowired
	private BlogSearchService blogSearchService; 
	
	@CrossOrigin
	@GetMapping("/autofill")
	public List<String> getAutofill(@RequestParam("q") String query) { // @formatter:off
		logger.info("[" + new Date() + "] autofill query: " + query);
		return blogSearchService.getAutoFillContent(query)
				.stream()
				.map(e -> e.getContent())
				.map(content -> {
					String contentLowerCase = content.toLowerCase();
					String queryLowerCase = query.toLowerCase();
					int startIndex = contentLowerCase.indexOf(queryLowerCase);
					int maxIndex = contentLowerCase.length() - 1;
					int endIndex = Math.min(startIndex + 50, maxIndex);
					while (contentLowerCase.charAt(endIndex) != ' ' && endIndex < maxIndex) 
						endIndex++;
					return contentLowerCase.substring(startIndex, endIndex);
				})
				.collect(Collectors.toList());
	} // @formatter:on

	@CrossOrigin
	@GetMapping("/blogs")
	public BlogListResponse getBlogs(@RequestParam("q") String query, @RequestParam("page") int page) { // @formatter:off
		logger.info("[" + new Date() + "] blogs query: " + query + " page: " + page);
		List<ResultBlog> list = blogSearchService.getBlogsByPage(query, page)
			.stream()
			.map(ResultBlog::new)
			.collect(Collectors.toList());
		return new BlogListResponse(list, blogSearchService.getTotalPages(query));
	} // @formatter:on
	
	@Getter
	@Setter
	@AllArgsConstructor
	public class BlogListResponse {
		private List<ResultBlog> list;
		private long size;
	}

	@Getter
	@Setter
	public class ResultBlog {

		private int blogId;
		private String content;
		private String title;
		private String url;
		private String date;
		private String authorName;
		private String authorJob;
		private String authorImageUrl;

		public ResultBlog(BlogContent blogContent) {
			this.content = blogContent.getContent();
			Blog blog = blogSearchService.getBlogById(blogContent.getBlogId());
			this.blogId = blog.getId();
			this.title = blog.getTitle();
			this.url = blog.getUrl();
			this.date = blog.getDate();
			Author author = blogSearchService.getAuthorById(blog.getAuthorId());
			this.authorName = author.getName();
			this.authorJob = author.getJob();
			this.authorImageUrl = author.getImageUrl();
		}
	}

}
