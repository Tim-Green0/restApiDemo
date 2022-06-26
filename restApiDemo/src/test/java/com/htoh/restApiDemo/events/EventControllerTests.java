package com.htoh.restApiDemo.events;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htoh.restApiDemo.common.TestDescription;

@RunWith(SpringRunner.class)
//@WebMvcTest // Web용 Bean 들만 등록해줌(Repository용 Bean들은 등록해주지 않음)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
//	@MockBean // mock 객체이기 때문에 save되는 값이 전부 null
//	EventRepository eventRepository;
	
	
	@Test
	@TestDescription("정상적으로 이벤트를 생성하는 테스트")
	public void createEvent() throws Exception {
		EventDto event = EventDto.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2022,10,23,11,11))
				.colseEnrollmentDateTime(LocalDateTime.of(2022,10,25,11,11))
				.beginEventDateTime(LocalDateTime.of(2022,10,30,11,11))
				.endEventDateTime(LocalDateTime.of(2022,10,31,11,11))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 Startup Factory")
				.build();
//		Mockito.when(eventRepository.save(event)).thenReturn(event);
		
		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON_VALUE)
				.content(objectMapper.writeValueAsString(event))
				)
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(jsonPath("id").exists())
		.andExpect(header().exists(HttpHeaders.LOCATION))
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE+";charset=utf-8"))
		.andExpect(jsonPath("id").value(Matchers.not(100)))
		.andExpect(jsonPath("free").value(Matchers.not(true)))
		.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
		;
	}
	
	@Test
	@TestDescription("입력받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
	public void createEvent_Bad_Request() throws Exception {
		Event event = Event.builder()
				.id(100)
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2022,10,23,11,11))
				.colseEnrollmentDateTime(LocalDateTime.of(2022,10,25,11,11))
				.beginEventDateTime(LocalDateTime.of(2022,10,30,11,11))
				.endEventDateTime(LocalDateTime.of(2022,10,31,11,11))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 Startup Factory")
				.free(true)
				.offline(false)
				.eventStatus(EventStatus.PUBLISHED)
				.build();
//		Mockito.when(eventRepository.save(event)).thenReturn(event);
		
		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON_VALUE)
				.content(objectMapper.writeValueAsString(event))
				)
		.andDo(print())
		.andExpect(status().isBadRequest())
		;
	}
	
	@Test
	@TestDescription("입력 값이 비여있는 경우에 에러가 발생하는 테스트")
	public void createEvent_Bad_Request_Empty_Input() throws Exception {
		EventDto eventDto = EventDto.builder().build();
		
		this.mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(eventDto))
				)
			.andDo(print())
			.andExpect(status().isBadRequest())
			;
	}
	
	@Test
	@TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
	public void createEvent_Bad_Request_Wrong_Input() throws Exception {
		EventDto eventDto = EventDto.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2022,10,26,11,11))
				.colseEnrollmentDateTime(LocalDateTime.of(2022,10,25,11,11))
				.beginEventDateTime(LocalDateTime.of(2022,10,24,11,11))
				.endEventDateTime(LocalDateTime.of(2022,10,23,11,11))
				.basePrice(10000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 Startup Factory")
				.build();
		
		this.mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(eventDto))
				)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$[0].objectName").exists())
//			.andExpect(jsonPath("$[0].field").exists())
			.andExpect(jsonPath("$[0].defaultMessage").exists())
			.andExpect(jsonPath("$[0].code").exists())
//			.andExpect(jsonPath("$[0].rejectedValue").exists())
			;
	}
}
