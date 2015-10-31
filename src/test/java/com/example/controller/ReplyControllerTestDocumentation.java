package com.example.controller;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.example.SpringRestDocsApplication;
import com.example.domain.Reply;
import com.example.repository.ReplyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringRestDocsApplication.class)
@WebAppConfiguration
@Transactional
public class ReplyControllerTestDocumentation {

	
	public static final String COMMENT = "comment";
	public static final String ARAHANSA = "arahansa";

	public static final String HELLO_WORLD = "Hello world";
	public static final String AUTHOR = "author";
	public static final String ID = "id";
	@Autowired
    private WebApplicationContext wac;
	@Autowired 
	private ObjectMapper objectMapper;
	@Autowired
	private ReplyRepository replyRepository;

	public RestDocumentationResultHandler document;

	
	@Rule
	public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");
	
   
    private MockMvc mockMvc;
    
    
    @Before
    public void setUp() {
		this.document = document("{method-name}",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()));
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
        		.apply(documentationConfiguration(this.restDocumentation))
        		.build();
    }
    
    
    @Test
	public void createReply() throws Exception {
    	Reply reply = new Reply();
    	reply.setAuthor(ARAHANSA);
    	reply.setComment(HELLO_WORLD);

    	mockMvc.perform(post("/comment").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reply)))
    			.andDo(print())
    			.andDo(this.document.snippets(
						links(
								linkWithRel(AUTHOR).optional().description("글쓴이"),
								linkWithRel(COMMENT).optional().description("댓글내용")
						),
						requestFields(
								fieldWithPath(ID).type(JsonFieldType.STRING).description("글쓴이 아이디"),
								fieldWithPath(AUTHOR).description("요청 글쓴이"),
								fieldWithPath(COMMENT).description("요청하는 댓글 내용")
						),
						responseFields(
							fieldWithPath(ID).type(JsonFieldType.STRING).description("글쓴이 아이디"),
							fieldWithPath(AUTHOR).type(JsonFieldType.STRING).description("글쓴이 응답"),
							fieldWithPath(COMMENT).type(JsonFieldType.STRING).description("글쓴이댓글 응답")
						)
				))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.author").value(ARAHANSA))
    			.andExpect(jsonPath("$.comment").value(HELLO_WORLD));
	}
    
    @Test
	public void getReplyandDelete() throws Exception {
		Reply saveSampleReply = saveSampleReply();
		mockMvc.perform(get("/comment/{id}", saveSampleReply.getId()).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.id").value(saveSampleReply.getId().intValue()))
    			.andExpect(jsonPath("$.author").value(ReplyControllerTestDocumentation.ARAHANSA))
    			.andExpect(jsonPath("$.comment").value(HELLO_WORLD));
		
		mockMvc.perform(delete("/comment/{id}", saveSampleReply.getId()))
			.andDo(print())
				.andDo(this.document.snippets(
						pathParameters(
								parameterWithName(ID).description("삭제 댓글 번호")
						)
				))
			.andExpect(status().isNoContent());
	}
    
    @Test
	public void simpleUpdateTest() throws Exception {
    	Reply reply = new Reply();
    	reply.setAuthor(ReplyControllerTestDocumentation.ARAHANSA);
    	reply.setComment("niHao");
    	Reply saveSampleReply = saveSampleReply();
    	
    	mockMvc.perform(put("/comment/{id}", saveSampleReply.getId()).accept(MediaType.APPLICATION_JSON)
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(reply)))
    			.andDo(print())
				.andDo(this.document.snippets(
						responseFields(
								fieldWithPath(ID).type(JsonFieldType.STRING).description("글쓴이 아이디"),
								fieldWithPath(AUTHOR).type(JsonFieldType.STRING).description("글쓴이 응답"),
								fieldWithPath(COMMENT).type(JsonFieldType.STRING).description("글쓴이댓글 응답")
						)
				))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.author").value(ReplyControllerTestDocumentation.ARAHANSA))
    			.andExpect(jsonPath("$.comment").value("niHao"));
	}
    
    private Reply saveSampleReply(){
    	Reply reply = new Reply();
    	reply.setAuthor(ReplyControllerTestDocumentation.ARAHANSA);
    	reply.setComment(HELLO_WORLD);
    	Reply saveReply = replyRepository.save(reply);
    	return saveReply;
    }



}
