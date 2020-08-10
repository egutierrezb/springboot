package com.interview.moonphasesapi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
public class MoonPhasesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoonPhasesApiApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(MoonPhasesController moonPhasesController)
	{   //Feeding with some sample test-cases, so we have
		//a-priori some records that will allow us to retrieve
		//some records if we may want.
		return args->{
			Stream.of("January","February","March").forEach(month->{
				Map<String,String> map = new HashMap<String, String>();
				map.put("newMoon","1");
				map.put("firstQuarter","2");
				map.put("fullMoon","3");
				map.put("thirdQuarter","4");
				MoonPhases moonPhasesMonth2004 = new MoonPhases(month, 2004, map);
				try {
					moonPhasesController.saveMoonPhase(moonPhasesMonth2004);
				} catch(Exception e) {
					System.out.println(e);
				}
				
				map.put("newMoon","3");
				map.put("firstQuarter","4");
				map.put("fullMoon","5");
				map.put("thirdQuarter","6");
				MoonPhases moonPhasesMonth2005 = new MoonPhases(month, 2005, map);
				try {
					moonPhasesController.saveMoonPhase(moonPhasesMonth2005);
				} catch(Exception e) {
					System.out.println(e);
				}	
			});
			moonPhasesController.getMoonPhases().forEach(System.out::println);
			
		};
	}
	
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				//Look at the apis that are inside /api only
				.paths(PathSelectors.regex("/api.*"))
				//Package based configuration. Select APIs that are inside of this package
				.apis(RequestHandlerSelectors.basePackage("com.interview.moonphasesapi"))
				.build()
				.apiInfo(apiDetails());
	}
	
	private ApiInfo apiDetails() {
		return new ApiInfo(
				"MoonPhase API",
				"API for obtaining the phases of the moon according to given parameters (year, month) or not at all",
				"1.0",
				"Free to use",
				new springfox.documentation.service.Contact("Alex gutierrez",  "http://linkedin.com/enriquegutierrezblancarte", "egutierrezb@gmail.com"),
				"No license",
				"http://localhost:8080/swagger-ui.html",
				Collections.emptyList());
	}
	

}
