package restApiDemo;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.htoh.restApiDemo.events.Event;

public class EventTest {

	@Test
	public void builder() {
		Event event = Event.builder()
				.name("Inflearn Spring REST API")
				.description("REST API development with Spring")
				.build();
		
		assertThat(event).isNotNull();
	}
	
	@Test
	public void javaBean() {
		Event event = new Event();
		String name = "Event";
		String description = "Spring";
		
		event.setName("Event");
		event.setDescription("Spring");
		
		assertThat((event.getName()).equals(name));
		assertThat(event.getDescription().equals(description));
	}

}