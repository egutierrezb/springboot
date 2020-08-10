package com.interview.moonphasesapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoonPhasesApiApplicationTests {
	
	@Autowired
	private MoonPhasesController moonPhasesController;
	
	/*Test to check that controller
	 *exists
	 */

	@Test
	public void contextLoads() {
		assertThat(moonPhasesController).isNotNull();
	}

}
