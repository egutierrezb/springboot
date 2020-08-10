package com.interview.moonphasesapi;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;



public class MoonPhasesRestControllerIntegrationTest extends MoonPhasesApiApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	/*
	 * Prepare settings/variables before our tests get executed
	 */

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	/*
	 * Test for verifying that an EndPoint is  an API EndPoint
	 */
	@Test
	public void isApiPointDefined() throws Exception {
	      String uri = "/api/";
	      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
	         .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	      
	      int status = mvcResult.getResponse().getStatus();
	      assertEquals(200, status);
	}
	
	/*
	 * Test for verifying that an EndPoint is an API EndPoint
	 */
	@Test
	public void notValidApiEndPoint() throws Exception {
		String uri = "/";
		mockMvc
	    .perform(MockMvcRequestBuilders.get(uri).accept(MediaType.TEXT_HTML))
	    .andExpect(status().isNotFound());
	}
	
	/*
	 * Test for verifying that we receive content in json format when fetching
	 * from /api/newmoon/2004
	 */
	@Test
	public void getNewMoonYearJSONFormat() throws Exception {
		mockMvc.perform(get("/api/newmoon/2004")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));
			
	}
	
	/*
	 * Test for verifying that we receive content in json format when fetching
	 * from /api/fullmoon/2005
	 */
	@Test
	public void getFullMoonYearJSONFormat() throws Exception {
		mockMvc.perform(get("/api/fullmoon/2005")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"));
			

	}
	
	/*
	 * Test for verifying that we have the correct data in the JSON array
	 * when accessing to /api/newmoon/2004
	 */
	@Test
	public void getNewMoonYearContents() throws Exception {
		mockMvc.perform(get("/api/newmoon/2004")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.newMoon", hasItem("January 1")))
				.andExpect(jsonPath("$.newMoon", hasItem("February 1")))
				.andExpect(jsonPath("$.newMoon", hasItem("March 1")));

	}
	
	/*
	 * Test for verifying that we have the correct data in the JSON array
	 * when accessing to /api/fullmoon/2005
	 */
	@Test
	public void getFullMoonYearContents() throws Exception {
		mockMvc.perform(get("/api/fullmoon/2005")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.fullMoon", hasItem("January 5")))
				.andExpect(jsonPath("$.fullMoon", hasItem("February 5")))
				.andExpect(jsonPath("$.fullMoon", hasItem("March 5")));
	}
	
	/*
	 * Test for verifying that we have the correct data in the JSON array
	 * when accessing to /api/January/2004
	 */
	@Test
	public void getMoonPhasesPerYearAndMonth() throws Exception {
		mockMvc.perform(get("/api/January/2004")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.moonPhases.firstQuarter").value("2"))
				.andExpect(jsonPath("$.moonPhases.fullMoon").value("3"))
				.andExpect(jsonPath("$.moonPhases.thirdQuarter").value("4"))
				.andExpect(jsonPath("$.moonPhases.newMoon").value("1"));
	}

	/*
	 * Test for verifying that we have the correct data in the JSON array
	 * when accessing to /api/January/{invalidDate}
	 */
	@Test
	public void getMoonPhasesPerInvalidYear() throws Exception {
		String invalidDate = String.valueOf(Integer.MAX_VALUE);
		mockMvc.perform(get("/api/January/"+invalidDate)).andExpect(status().isOk())
				.andExpect(content().string(containsString("{\"moonPhases\":null}")));
	}
	
	/*
	 * Test for verifying that we have the correct data in the JSON array
	 * when accessing to /api/{invalidMonth}/2004
	 */
	@Test
	public void getMoonPhasesPerInvalidMonth() throws Exception {
		String invalidMonth = "dummy";
		mockMvc.perform(get("/api/"+invalidMonth+"/2004")).andExpect(status().isOk())
				.andExpect(content().string(containsString("{\"moonPhases\":null}")));
	}
    
}
