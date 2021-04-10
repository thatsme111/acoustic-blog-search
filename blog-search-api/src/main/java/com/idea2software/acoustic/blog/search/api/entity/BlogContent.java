package com.idea2software.acoustic.blog.search.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class BlogContent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private int blogId;
	
	@Column(length = 10_000)
	private String content;
	
}
