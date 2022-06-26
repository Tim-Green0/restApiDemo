package com.htoh.restApiDemo.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE+";charset=utf-8")
public class EventContoller {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EventValidator eventValidator;
	
	@PostMapping
	public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}
		
		eventValidator.validate(eventDto, errors);
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}
		
		Event event = modelMapper.map(eventDto, Event.class);
		event.update();
		System.out.println(event.toString());
		Event newEvent = this.eventRepository.save(event);
		URI createUri = linkTo(EventContoller.class).slash(newEvent.getId()).toUri(); // slash에 넣는 값을 상대로 pathVariable 변경
		return ResponseEntity.created(createUri).body(newEvent);
	}
	
}
