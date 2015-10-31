package com.example.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data @EqualsAndHashCode(of="id")
public class Reply {
	@Id
	@GeneratedValue
	private Long id;
	
	private String author;
	
	private String comment;
	
}
