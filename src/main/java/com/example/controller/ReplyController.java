package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Reply;
import com.example.repository.ReplyRepository;



@RestController
@RequestMapping("/comment")
public class ReplyController {

	@Autowired ReplyRepository replyRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Reply> replyList(){
		return replyRepository.findAll();
	}
	
	// Create
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity postReply(@RequestBody Reply reply){
		Reply createdReply = replyRepository.save(reply);
		return new ResponseEntity<>(createdReply, HttpStatus.OK);
	}
	
	// Read
	@RequestMapping(value="/{id}" , method=RequestMethod.GET)
	public Reply readOne(@PathVariable Long id){
		return replyRepository.findOne(id);
	}
	
	// Update 
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Reply> update
		(@PathVariable Long id, @RequestBody Reply reply){
		replyRepository.updateReply(reply.getComment(), id);
		return new ResponseEntity<>(reply, HttpStatus.OK);
	}
	
	// Delete
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity deleteReply(@PathVariable Long id){
		replyRepository.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
