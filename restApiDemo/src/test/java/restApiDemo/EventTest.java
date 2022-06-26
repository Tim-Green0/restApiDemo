package restApiDemo;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import com.htoh.restApiDemo.events.Event;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
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
	
	@Test
	@Parameters
	//(method = "paramsForTestFree") 뒤에 이거 붙혀서 특정 Method가 해당 파라미터로 들어가게 할 수도 있지만
	public void testFree(int basePrice, int maxPrice, boolean isFree) {
		//Given
		Event event = Event.builder()
				.basePrice(basePrice)
				.maxPrice(maxPrice)
				.build();
		
		//When
		event.update();
		
		
		//Then
		assertThat(event.isFree()).isEqualTo(isFree);
	}
	
	//Method명 앞에 parametersFor 하고 뒤에 테스트하고 싶은 Method 명 붙히면
	//ex) parametersFor + TestFree ==> testFree Method에 해당 파라미터가 바인딩됨
	//위와 같은 기술을 컨벤션
	private Object[] parametersForTestFree() {
		return new Object[] {
			new Object[] {0, 0, true},
			new Object[] {0, 100, false},
			new Object[] {100, 0, false},
			new Object[] {100, 200, false}
		};
	}
	
	@Test
	@Parameters
	public void testOffline(String location, boolean isOffline) {
		//Given
		Event event = Event.builder()
				.location(location)
				.build();
		
		//When
		event.update();
		
		//Then
		assertThat(event.isOffline()).isEqualTo(isOffline);
	}
	
	private Object[] parametersForTestOffline() {
		return new Object[] {
			new Object[] {"강남역 네이버", true},
			new Object[] {null, false},
			new Object[] {"", false}
		};
	}

}
