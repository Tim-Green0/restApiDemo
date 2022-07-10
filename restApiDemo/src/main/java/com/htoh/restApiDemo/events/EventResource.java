package com.htoh.restApiDemo.events;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

//EntityModel 쓰면 하위에 JsonUnwrapped가 내장되어 있어서
//해당 상속받으면 자동으로 JsonUnWrapped 됨
public class EventResource extends EntityModel<Event> {
	
	public EventResource(Event event, Link... links) {
		super(event, links);
		add(linkTo(EventContoller.class).slash(event.getId()).withSelfRel());
	}
}
