package com.rest.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fakemongo.Fongo;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder;
import com.mongodb.Mongo;
import com.rest.resource.Address;
import com.rest.resource.Alumn;
import com.rest.resource.AlumnRepository;
import com.rest.resource.Application;
import com.rest.resource.Education;
import com.rest.resource.Qualification;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class AlumniControllerTest {
	private static final Logger log = LoggerFactory.getLogger(AlumniControllerTest.class);

	
	/*********************** TEST DATA *************************************/
	
	private static final Alumn A1 = new Alumn("Valerio", Arrays.asList(new Address("palmiro togliatti", "11", "Italy")), 
											new Education(Arrays.asList(new Qualification("Bachelor", "Catania", 2009))));
	
	private static final Alumn A2 = new Alumn("John", Arrays.asList(new Address("parlament road", "321", "United Kingdom")), 
			new Education(Arrays.asList(new Qualification("Master", "Cambridge", 2012))));

	private static final Alumn A3 = new Alumn("Ragnar", Arrays.asList(new Address("something", "65", "Norway")), 
			new Education(Arrays.asList(new Qualification("Master", "Oslo", 2008))));

	private static final Alumn A4 = new Alumn("Pier", Arrays.asList(new Address("bulevard rouge", "30", "France")), 
			new Education(Arrays.asList(new Qualification("Bachelor", "Sorbone", 2011))));

	private static final List<Alumn> LIST = Arrays.asList(A1, A2, A3, A4);
	/**********************************************************************/
	

	@Autowired
	WebApplicationContext context;

	@Rule
    public MongoDbRule mongoDbRule = MongoDbRuleBuilder.newMongoDbRule().defaultSpringMongoDb("test");

    @Autowired
    private AlumnRepository alumniRepo;

	private MockMvc mvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	
	/**
	 * Test Post - Create an item in the db
	 * Check number of items in db is incremented by 1 and search the object by id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPost() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		String content = mapper.writeValueAsString(A1);
		
		long count = alumniRepo.count();
		
		ResultActions ra = 
				mvc.perform(MockMvcRequestBuilders.post("/alumni")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content))
				.andExpect(status().is2xxSuccessful());
				
		String id = (String) ra.andReturn().getResponse().getHeaderValue("id");
		
		Assert.assertEquals(count+1, alumniRepo.count());
		Assert.assertEquals(true, alumniRepo.exists(id));
	}

	/**
	 * Test Put - Increment the quantity of the item with {id}
	 * Check the item quantity has been incremented after the put
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGet() throws Exception {
		alumniRepo.save(A1);
		String id = A1.getId().toString();
		
		//add quantity to i1
		mvc.perform(MockMvcRequestBuilders.delete("/alumni/"+id))
				.andExpect(status().isOk());
		
		Alumn i = alumniRepo.findOne(id);
//		Assert.assertEquals(a1.getQuantity()+1, i.getQuantity());
	}
	
	/**
	 * Test Delete - Delete the item with {id}
	 * Check the item does not exist anymore in the db
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {
		alumniRepo.save(A2);
		String id = A2.getId().toString();
		
		mvc.perform(MockMvcRequestBuilders.delete("/alumni/"+id))
				.andExpect(status().isOk());
		
		
		Assert.assertEquals(false, alumniRepo.exists(id));
	}
	
	/**
	 * Test Get over all the items in the db
	 * Check that every item in the db is present in the json output of the API
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetAll() throws Exception {
		alumniRepo.deleteAll();
		alumniRepo.save(LIST);

		Assert.assertThat(alumniRepo.findAll(), Matchers.is(LIST));
		
		ResultActions action = mvc.perform(MockMvcRequestBuilders.get("/alumni"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.content", Matchers.hasSize(LIST.size())));
			
		for (Alumn i : LIST) {
			action.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[*].id", Matchers.hasItem(i.getId())));
		}
	}
	
	
	/**
	 * Test Get over the items in the db filtered by education
	 * Check that every item in the db which has the selected education is present in the json output of the API
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetByEducation() throws Exception {
		final String education = "Master";
		alumniRepo.deleteAll();
		alumniRepo.save(LIST);

		Assert.assertThat(alumniRepo.findAll(), Matchers.is(LIST));
		
		ResultActions action = mvc.perform(MockMvcRequestBuilders.get("/alumni").param("education", education))
				.andExpect(status().isOk());
			
		for (Alumn i : LIST) {
			if (i.getEducation().equals(education)) {
				action.andExpect(MockMvcResultMatchers.jsonPath("$.data[*].id", Matchers.hasItem(i.getId())));
			} else {
				action.andExpect(MockMvcResultMatchers.jsonPath("$.data[*].id", Matchers.not(Matchers.hasItem(i.getId()))));
			}
		}
	}
	
	
	/**
	 * Test Get over the items in the db filtered by name
	 * Check that every item with the selected name in the db is present in the json output of the API
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetByName() throws Exception {
		final String name = "Valerio";
		alumniRepo.deleteAll();
		alumniRepo.save(LIST);

		Assert.assertThat(alumniRepo.findAll(), Matchers.is(LIST));
		
		ResultActions action = mvc.perform(MockMvcRequestBuilders.get("/alumni").param("name", name))
				.andExpect(status().isOk());
			
		for (Alumn a : LIST) {
			if (name.equals(a.getName())) {
				action.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[*].id", Matchers.hasItem(a.getId())));
			} else {
				action.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[*].id", Matchers.not(Matchers.hasItem(a.getId()))));
			}
		}
	}
	
	
	/**
	 * Test Get over the items in the db filtered by name and education
	 * Check which combines the two previous cases
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetByNameAndEducation() throws Exception {
		final String education = "Bachelor";
		final String name = "Valerio";
		final Qualification q = new Qualification(education, "", 0);
		
		alumniRepo.deleteAll();
		alumniRepo.save(LIST);

		Assert.assertThat(alumniRepo.findAll(), Matchers.is(LIST));
		
		ResultActions action = mvc.perform(MockMvcRequestBuilders.get("/alumni")
				.param("education", education)
				.param("name", name))
				.andExpect(status().isOk());
			
		log.info(action.andReturn().getResponse().getContentAsString());
		
		for (Alumn a : LIST) {
			if (name.equals(a.getName()) && a.getEducation().getQuaifications().contains(q)) {
				action.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[*].id", Matchers.hasItem(a.getId())));
			} else {
				action.andExpect(MockMvcResultMatchers.jsonPath("$.data.content[*].id", Matchers.not(Matchers.hasItem(a.getId()))));
			}
		}
	}
	
	
	@Configuration
	@EnableMongoRepositories
	@ComponentScan(basePackageClasses = {AlumnRepository.class})
	static class RepositoryTestConfiguration extends AbstractMongoConfiguration {

	    @Override
	    protected String getDatabaseName() {
	        return "demo-test";
	    }
		
	    @Override
		public Mongo mongo() {
	    	// uses fongo for in-memory tests
			return new Fongo("mongo-test").getMongo();
		}
	}
}