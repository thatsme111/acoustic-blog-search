package com.idea2software.acoustic.blog.search.api;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idea2software.acoustic.blog.search.api.repository.BlogContentRepository;

@RestController
public class BlogSearchController {

	private static final Logger logger = LoggerFactory.getLogger(BlogSearchController.class);

	@Autowired
	private BlogContentRepository blogContentRepository;

	@GetMapping("/autofill")
	public List<String> getAutofill(@RequestParam("q") String query) { // @formatter:off
		logger.info("[" + new Date() + "] autofill query: " + query);
		return blogContentRepository.findTop10ByContentContainingIgnoreCase(query)
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

}
