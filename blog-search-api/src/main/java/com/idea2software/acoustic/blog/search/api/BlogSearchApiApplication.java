package com.idea2software.acoustic.blog.search.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.idea2software.acoustic.blog.search.api.entity.Author;
import com.idea2software.acoustic.blog.search.api.entity.Blog;
import com.idea2software.acoustic.blog.search.api.entity.BlogContent;
import com.idea2software.acoustic.blog.search.api.handler.SavedBlog;
import com.idea2software.acoustic.blog.search.api.handler.SavedBlog.SavedAuthor;
import com.idea2software.acoustic.blog.search.api.handler.SavedBlogsHandler;
import com.idea2software.acoustic.blog.search.api.repository.AuthorRepository;
import com.idea2software.acoustic.blog.search.api.repository.BlogContentRepository;
import com.idea2software.acoustic.blog.search.api.repository.BlogRepository;

@SpringBootApplication
public class BlogSearchApiApplication {

	private static final Logger logger = LoggerFactory.getLogger(BlogSearchApiApplication.class);

	@Autowired
	private SavedBlogsHandler savedBlogsHandler;

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BlogContentRepository blogContentRepository;

	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup() {
		logger.info("Importing initial blog data into the database...");
		for (SavedBlog savedBlog : savedBlogsHandler.getSavedBlogs()) {
			SavedAuthor savedAuthor = savedBlog.getAuthor();

			Author author = new Author();
			author.setName(savedAuthor.getName());
			author.setJob(savedAuthor.getJob());
			author.setImageUrl(savedAuthor.getImageUrl());
			authorRepository.save(author);

			Blog blog = new Blog();
			blog.setTitle(savedBlog.getTitle());
			blog.setUrl(savedBlog.getUrl());
			blog.setDate(savedBlog.getDate());
			blog.setAuthorId(author.getId());
			blogRepository.save(blog);

			for (String content : savedBlog.getContent()) {
				BlogContent blogContent = new BlogContent();
				blogContent.setBlogId(blog.getId());
				blogContent.setContent(content);
				blogContentRepository.save(blogContent);
			}
		}
		logger.info("Data Import Completed.");
		
//		Stream<BlogContent> stream = StreamSupport.stream(blogContentRepository.findAll().spliterator(), false);
//		AtomicInteger runner = new AtomicInteger(0);
//		stream.forEach(e -> {
//			System.out.println(e);
//			runner.incrementAndGet();
//		});
//		System.out.println("runner: " + runner.get());
		
//		List<BlogContent> list = blogContentRepository.findTop10ByContentContainingIgnoreCase("COVID-19");
//		list.forEach(System.out::println);
//		System.out.println("size: " + list.size());
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BlogSearchApiApplication.class, args);
	}

}
