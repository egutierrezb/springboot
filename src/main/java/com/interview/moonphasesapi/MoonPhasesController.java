package com.interview.moonphasesapi;

import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
public class MoonPhasesController {
	
	public final static int MAXIMUM_ENTRIES_MOONPHASES = 8;
	
	@Autowired
	public MoonPhasesRepository moonPhasesRepository;
	
	@Autowired
	public MoonPhasesList moonPhasesList;
	
	
	@ApiOperation(value = "Get all  moon phase records for a given year and month", notes= "Provide valid year and month values")
	@GetMapping("/{month}/{year}")
	public MoonPhasesList getMoonPhasesByMonthAndYear(
			@ApiParam(required = true, name = "month", value = "Month for querying moon phase records", example = "January")
			@PathVariable("month") String month,
			@ApiParam(required = true, name = "year", value = "Year for querying moon phase records", example = "2004")
			@PathVariable("year") String year)
	{
		Map<String, String> moonPhases = null;
		List<MoonPhases> moonPhasesListAll = (List<MoonPhases>)moonPhasesRepository.findAll();
		List<MoonPhases> moonPhasesFiltered = moonPhasesListAll.stream().filter(moonPhase -> moonPhase.getMonth().equals(month) && moonPhase.getYear()==Integer.parseInt(year)).collect(Collectors.toList());
		for(MoonPhases eachMoonPhase : moonPhasesFiltered )
		{
			moonPhases = eachMoonPhase.getMoonPhasesYearMonth();
		}
		//Invoke the wrapper class, so instead of
		//getting a Map directly (i.e. moonPhases)
		//we will return the wrapper object
		//at the end of this method
		//-display it as a JSON Object.
		moonPhasesList.setMoonPhases(moonPhases);
		return moonPhasesList;
	}
	
	@ApiOperation(value = "Find all moonPhases for all years and months", notes= "Do not need any parameter", response = MoonPhases.class)
	@GetMapping("/")
	public List<MoonPhases> getMoonPhases()
	{
		return (List<MoonPhases>)moonPhasesRepository.findAll();
	}
	
	@ApiOperation(value = "Get all new moon phase records for a given year", notes= "Provide a valid year")
	@GetMapping(value="/newmoon/{year}", produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getNewMoonInMonthsAndDays(
			@ApiParam(required = true, name = "year", value = "Year for querying new moon phase records", example = "2004")
			@PathVariable("year") String year)
	{
		return getFullMoonInMonthsAndDaysUtil("newMoon",year);
	}
	
	@ApiOperation(value = "Get all full moon phase records for a given year", notes= "Provide a valid year")
	@GetMapping(value="/fullmoon/{year}", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getFullMoonInMonthsAndDays(
			@ApiParam(required = true, name = "year", value = "Year for querying full moon phase records", example = "2004")
			@PathVariable("year") String year)
	{
		return getFullMoonInMonthsAndDaysUtil("fullMoon",year);
	}
	
	@PostMapping("/moonPhase")
	@ApiOperation(value = "Save in the memory db a MoonPhase Object", notes= "Provide a valid MoonPhase Object in JSON Format")
	public void saveMoonPhase(
			@ApiParam(required = true, name = "moonPhases", value = 
			"JSON Object that represents a Moon Phase for given year and month values; moonPhasesYearMonth"
			+ " collection can assume any of the following entries: \"newMoon\",\"waxingCrescent\",\"firstQuarter\",\"waxingGibbous\",\"fullMoon\",\"waningGibbous\",\"thirdQuarter\",\"waningCrescent\" \n"
			+ "; months can be: \"January\",\"February\",\"March\",\"April\", \"June\", \"July\", \"August\", \"September\", \"October\", \"November\", \"December\". \n" 
			+ "A day for a moon phase can be a number from [1, 30]. (e.g. newMoon: 23). \n"
			+ "If the above rules are not followed, an exception is thrown."
			+ "A correct example can be taken from below, \n"+
					"{ \n" + 
            		"	'id': 1, \n" + 
            		"	'month': 'January', \n" + 
            		"	'moonPhasesYearMonth': { \n" + 
            		"	'newMoon': '1', \n" + 
            		"	'firstQuarter': '2', \n" + 
            		"	'fullMoon': '3', \n" + 
            		"	'thirdQuarter':'4', \n" + 
            		"}, \n" + 
            		"	'year': 2004 \n" + 
            		"}"
        )	
			@RequestBody MoonPhases moonPhase) throws Exception
	{
		//Validation of the JSON Object before doing a POST Method
		validationBeforePostingUtil(moonPhase);
		//If goes through all the validations, it is safe NOW to POST JSON Object
		moonPhasesRepository.save(moonPhase);
	}
	
	/* Utility methods */
	private void validationBeforePostingUtil(MoonPhases moonPhase) throws Exception
	{
		boolean isValid = false;
		
		//Validation for keys in the Map mapPhases of the moonPhaseObject
		Map<String, String> mapPhases =moonPhase.getMoonPhasesYearMonth();
		List<String> validMoonPhases = Arrays.asList("newMoon","waxingCrescent","firstQuarter","waxingGibbous","fullMoon","waningGibbous","thirdQuarter","waningCrescent");
		
		for(Map.Entry<String, String> e: mapPhases.entrySet() )
		{
			if(!validMoonPhases.contains((e.getKey())))
				throw new Exception("JSON object can not be POSTED due to an invalid key. Valid keys are: "
						+ "\"newMoon\",\"waxingCrescent\",\"firstQuarter\",\"waxingGibbous\",\"fullMoon\",\"waningGibbous\",\"thirdQuarter\",\"waningCrescent\"");
		}
		List<String> validMonthsPhases = Arrays.asList("January","February","March","April", "May", "June", "July", "August", "September", "October", "November", "December");
		
		for(String validMonthPhase: validMonthsPhases)
		{
			if(validMonthPhase.equals(moonPhase.getMonth()))
			{
				isValid = true;
			}
		}
		if(!isValid)
		{
			throw new Exception("JSON object can not be POSTED since it does not contain a valid month. ");
		}
		for(Map.Entry<String, String> e: mapPhases.entrySet() )
		{
			if(Integer.parseInt(e.getValue()) < 1 || Integer.parseInt(e.getValue()) > 30)
			{
				throw new Exception("JSON object can not be POSTED since it does not contain a valid day in one or in many entries");
			}
		}
		//Validation in terms of the size, should it contain the all moonphases?
		/*if(mapPhases.size()< MAXIMUM_ENTRIES_MOONPHASES)
		{
			throw new Exception("JSON object can not be POSTED since it doesn´t contain the ALL moon phases");
		}*/
		
	}
	
	private @ResponseBody String getFullMoonInMonthsAndDaysUtil(String phase, String year)
	{
	 	List<String> moonPhasesDayMonth = new ArrayList<>();
		List<MoonPhases> moonPhasesList = (List<MoonPhases>)moonPhasesRepository.findAll();
		List<MoonPhases> moonPhasesFiltered = moonPhasesList.stream().filter(moonPhase -> moonPhase.getYear()==Integer.parseInt(year)).collect(Collectors.toList());
		for(MoonPhases eachMoonPhase : moonPhasesFiltered )
		{
			String dayMoonPhasePerYear = eachMoonPhase.getMoonPhasesYearMonth().get(phase);
			String monthMoonPhasePerYear = eachMoonPhase.getMonth();
			moonPhasesDayMonth.add("\""+monthMoonPhasePerYear+" "+dayMoonPhasePerYear+"\"");
		
		}
	
		return "{ \""+phase+"\": "+Arrays.toString(moonPhasesDayMonth.toArray())+" }";

	}
}
