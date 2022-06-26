package com.htoh.restApiDemo.events;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {
	
	public void validate(EventDto eventDto, Errors errors) {
		if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
//			errors.rejectValue("basePrice", "wrongValue", "BasePrice is worng.");
//			errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is worng.");
			errors.reject("wrongPrices", "Value for Prices are wrong");
		}
		
		LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
		if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) || 
			endEventDateTime.isBefore(eventDto.getColseEnrollmentDateTime()) ||
			endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()))	{
			
			errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong.");
		}
		
		//TODO BeginEventDateTime
		//TODO CloseEnrollmentDateTime
		
	}
}
