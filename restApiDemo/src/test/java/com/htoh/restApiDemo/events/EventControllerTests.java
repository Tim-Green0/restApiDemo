package com.htoh.restApiDemo.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	public void createEvent() throws Exception {
		Event event = Event.builder()
				.id(100)
				.name("Spring")
				.description("REST API Development with Spring") 
				.beginEnrollmentDateTime(LocalDateTime.of(2022, 02, 16, 11, 11))
				.endEventDateTime(LocalDateTime.of(2022, 03, 01, 11, 11))
				.beginEventDateTime(LocalDateTime.of(2022, 03, 02, 11, 11))
				.endEventDateTime(LocalDateTime.of(2022, 04, 01, 11, 11))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("한글")
				.free(true)
				.offline(false)
				.build();
		
		mockMvc.perform(post("/api/events")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaTypes.HAL_JSON)
					.content(objectMapper.writeValueAsString(event)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").exists())
				.andExpect(header().exists("Location"))
				.andExpect(header().string("Content-Type", "application/hal+json;charset=utf8"))
//				.andExpect(jsonPath("id").value(Matchers.not(10)))
				.andExpect(jsonPath("free").value(Matchers.not(true)))
				.andExpect(jsonPath("id").value(Matchers.not(100))
				.andExpect(jsonPath("free").value(Matchers.not(true))
						)
				;
		
	}
}
