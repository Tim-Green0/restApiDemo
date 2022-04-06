package com.htoh.restApiDemo.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE+";charset=utf8")
public class EventContoller {
	
	@Autowired
	EventRepository eventRepository;
	
	@PostMapping
	public ResponseEntity creatEvent(@RequestBody EventDto eventDto) {
		Event event = new ModelMapper().map(eventDto, Event.class);
		Event newEvent = this.eventRepository.save(event);
		URI createdUri = linkTo(EventContoller.class).slash(newEvent.getId()).toUri();
		System.out.println("");
		return ResponseEntity.created(createdUri).body(event);
	}
	
}
