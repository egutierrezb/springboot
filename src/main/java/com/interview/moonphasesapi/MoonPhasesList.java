package com.interview.moonphasesapi;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
//Wrapper Class
public class MoonPhasesList {
	private Map<String, String> moonPhases;

	public Map<String, String> getMoonPhases() {
		return moonPhases;
	}

	public void setMoonPhases(Map<String, String> moonPhases) {
		this.moonPhases = moonPhases;		
	}
	

}
