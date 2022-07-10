package com.htoh.restApiDemo.events;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htoh.restApiDemo.common.RestDocsConfiguration;
import com.htoh.restApiDemo.common.TestDescription;
import com.sun.xml.txw2.Document;

@RunWith(SpringRunner.class)
//@WebMvcTest // Web용 Bean 들만 등록해줌(Repository용 Bean들은 등록해주지 않음)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
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
		.andExpect(header().exists(HttpHeaders.LOCATION))
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE+";charset=utf-8"))
		.andExpect(jsonPath("id").value(Matchers.not(100)))
//		.andExpect(jsonPath("free").value(Matchers.not(true)))
		.andExpect(jsonPath("free").value(false))
		.andExpect(jsonPath("offline").value(true))
		.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
		.andExpect(jsonPath("_links.self").exists())
		.andExpect(jsonPath("_links.query-events").exists())
		.andExpect(jsonPath("_links.update-events").exists())
		.andDo(document("create-Event",
				links(
						linkWithRel("self").description("link to self"),
						linkWithRel("query-events").description("link to query events"),
						linkWithRel("update-events").description("link to update an existing event")
				),
				requestHeaders(
						headerWithName(HttpHeaders.ACCEPT).description("accept header"),
						headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
				),
				requestFields(
						fieldWithPath("name").description("Name of new Event"),
						fieldWithPath("description").description("description of new Event"),
						fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new Event"),
						fieldWithPath("colseEnrollmentDateTime").description("colseEnrollmentDateTime of new Event"),
						fieldWithPath("beginEventDateTime").description("beginEventDateTime of new Event"),
						fieldWithPath("endEventDateTime").description("endEventDateTime of new Event"),
						fieldWithPath("location").description("location of new Event"),
						fieldWithPath("basePrice").description("basePrice of new Event"),
						fieldWithPath("maxPrice").description("maxPrice of new Event"),
						fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new Event")
				),
				responseHeaders(
						headerWithName(HttpHeaders.LOCATION).description("Location header"),
						headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
				),
				responseFields(
						fieldWithPath("id").description("identifier of new Event"),
						fieldWithPath("name").description("Name of new Event"),
						fieldWithPath("description").description("description of new Event"),
						fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new Event"),
						fieldWithPath("colseEnrollmentDateTime").description("colseEnrollmentDateTime of new Event"),
						fieldWithPath("beginEventDateTime").description("beginEventDateTime of new Event"),
						fieldWithPath("endEventDateTime").description("endEventDateTime of new Event"),
						fieldWithPath("location").description("location of new Event"),
						fieldWithPath("basePrice").description("basePrice of new Event"),
						fieldWithPath("maxPrice").description("maxPrice of new Event"),
						fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new Event"),
						fieldWithPath("free").description("it tells if this event is free or not free"),
						fieldWithPath("offline").description("it tells if this event is offline or online"),
						fieldWithPath("eventStatus").description("eventStatus of new Event"),
						
						fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("link to self").optional(),
				        fieldWithPath("_links.query-events.href").type(JsonFieldType.STRING).description("link to query-events").optional(),
				        fieldWithPath("_links.update-events.href").type(JsonFieldType.STRING).description("link to update-events").optional()
				)
				))
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
