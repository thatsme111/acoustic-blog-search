package com.idea2software.acoustic.blog.search.api.handler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SavedBlogsHandler {

	private static final Logger logger = LoggerFactory.getLogger(SavedBlogsHandler.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	public List<SavedBlog> getSavedBlogs() { // @formatter:off
		return IntStream.range(0, 82)
				.boxed()
				.map(num -> "classpath:blogs/" + (num < 10 ? "0" : "") + num + ".json")
				.map(this::getFileFromResourceFolder)
				.map(this::toSavedBlogModel)
				.collect(Collectors.toList());
	} // @formatter:on

	public SavedBlog toSavedBlogModel(File file) {
		try {
			return mapper.readValue(file, SavedBlog.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private File getFileFromResourceFolder(String filePath) {
		try {
			return ResourceUtils.getFile(filePath);
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return null;
	}

}
