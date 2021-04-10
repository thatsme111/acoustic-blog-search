package com.idea2software.acoustic.blog.search.api.handler;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SavedBlog {

	private String url;
	private String title;
	private String date;
	private SavedAuthor author;
	private List<String> content;

	@Getter
	@Setter
	public class SavedAuthor {
		private String name;
		private String job;
		private String imageUrl;
	}

}
