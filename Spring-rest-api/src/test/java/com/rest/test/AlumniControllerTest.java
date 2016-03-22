package com.rest.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import com.rest.resource.AlumniController;
import com.rest.resource.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class AlumniControllerTest {

	@Autowired
	WebApplicationContext context;

	@InjectMocks
	AlumniController controller;

	private MockMvc mvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void chiamata1() throws Exception {

		String content = "{\"name\":\"nome\",\"addresses\":[{\"street\":\"streetname\", \"number\":\"22\", \"country\":\"country\"},{ \"street\":\"streetnamedue\", \"number\":\"33\", \"country\":\"country\"}],\"education\":{\"master\":{\"university\":\"Politecnico Milano\", \"year\": \"2004\"},\"phd\":{\"university\":\"UCSD\", \"year\": \"2009\"}}}";
		
		mvc.perform(post("/alumni")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content))
				.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void chiamata2() throws Exception {
		mvc.perform(get("/alumni"))
				.andExpect(status().isOk());
//				.andExpect(jsonPath("$.name", is("nome")))
//				.andExpect(jsonPath("$", hasSize(1)));
	}
	
}