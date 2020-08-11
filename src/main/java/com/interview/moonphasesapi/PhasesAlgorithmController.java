package com.interview.moonphasesapi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RequestMapping("/api2")
@RestController
public class PhasesAlgorithmController {
	
	@ApiOperation(value = "Get all  moon phase records for a given year and month", notes= "Provide valid year and month values")
	@GetMapping("/{month}/{year}")
	public Map<String, String> getByMonthAndYear(
			@ApiParam(required = true, name = "month", value = "Month for querying moon phase records", example = "January")
			@PathVariable("month") String month,
			@ApiParam(required = true, name = "year", value = "Year for querying moon phase records", example = "2004")
			@PathVariable("year") String year)
	{
		String[] phases = {"newMoon","waxingCrescent","firstQuarter","waxingGibbous","fullMoon","waningGibbous","thirdQuarter","waningCrescent"};
		String[] months = {"January","February","March","April", "May", "June", "July", "August", "September", "October", "November", "December"};
		
		//how many days February has?
		int daysFeb = daysInFeb(Integer.parseInt(year));
		
		Integer[] days = {31,daysFeb,31,30,31,30,31,31,30,31,30,31};
		
		//mapPhase will contain {phase, days}
		Map<String, String> mapPhases = new HashMap<>();
		
		//monthsMap map the number of days per month
		Map<String, Integer> monthsMap = new HashMap<>();
		int numberMonth = 0;
		for(int i=0; i<12; i++)
		{
			monthsMap.put(months[i], days[i]);
			if(months[i].equals(month))
			{
				numberMonth = i;
			}
		}
		
		int numDays = monthsMap.get(month);
		for(int day=1; day<=numDays; day++)
		{
			int intPhase = computeMoonPhase(Integer.parseInt(year),numberMonth,day);
			//Map will contain {phase, days} for a specific month/year pair
			if(mapPhases.get(phases[intPhase])==null)
			{
				mapPhases.put(phases[intPhase],String.valueOf(day));
			}
			else
			{
				StringBuffer sb = new StringBuffer();
				sb.append(mapPhases.get(phases[intPhase]));
				sb.append(" "+String.valueOf(day));
				mapPhases.put(phases[intPhase],sb.toString());
			}
		}
		return mapPhases;
		
	}
	
	@ApiOperation(value = "Get all full mooon  phase records for a given year", notes= "Provide a valid year")
	@GetMapping("/fullMoon/{year}")
	public String getFullMoonByYear(
			@ApiParam(required = true, name = "year", value = "Year for querying full moon phase records", example = "2004")
			@PathVariable("year") String year)
	{

		//filter only with fullMoon
		return filterByMoonPhase(year, "fullMoon");
		
	}
	
	@ApiOperation(value = "Get all new moon phase records for a given year", notes= "Provide a valid year")
	@GetMapping("/newMoon/{year}")
	public String getNewMoonByYear(
			@ApiParam(required = true, name = "year", value = "Year for querying new moon phase records", example = "2004")
			@PathVariable("year") String year)
	{

		//filter only with fullMoon
		return filterByMoonPhase(year, "newMoon");
		
	}
	
	/*
	 * Utility function
	 * Core code for filtering by moon phase
	 * given a specific year
	 */
	private String filterByMoonPhase(String year, String moonPhase)
	{
		String[] phases = {"newMoon","waxingCrescent","firstQuarter","waxingGibbous","fullMoon","waningGibbous","thirdQuarter","waningCrescent"};
		String[] months = {"January","February","March","April", "May","June", "July", "August", "September", "October", "November", "December"};
		
		//how many days February has?
		int daysFeb = daysInFeb(Integer.parseInt(year));
		
		Integer[] days = {31,daysFeb,31,30,31,30,31,31,30,31,30,31};
		
		//mapPhase will contain {phase, days}
		Map<String, String> mapPhases = new HashMap<>();
		Map<String, Integer> monthsMap = new HashMap<>();
		
		//monthsMap map the number of days per month
		for(int i=0; i<12; i++)
		{
			monthsMap.put(months[i], days[i]);
		}
		
		for(int month=1; month<=12; month++)
		{
			for(int day=1; day<=monthsMap.get(months[month-1]); day++)
			{
				int intPhase = computeMoonPhase(Integer.parseInt(year),month,day);
				//Map will contain {phase, days} for each month of the whole year
				if(mapPhases.get(phases[intPhase])==null)
				{
					mapPhases.put(phases[intPhase],months[month-1]+" "+String.valueOf(day)+" ");
				}
				else
				{
					StringBuffer sb = new StringBuffer();
					sb.append(mapPhases.get(phases[intPhase]));
					sb.append(months[month-1]+" "+String.valueOf(day)+" ");
					mapPhases.put(phases[intPhase],sb.toString());
				}
			}
			
		}
		return mapPhases.get(moonPhase);
	}
	
	/*
	 * Utility function
	 * Compute if February has 28 or 29 days
	 */
 
	private int daysInFeb(int year)
	{
		if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0))
		{
		    return 29;
		}
		else {
		    return 28;
		}
	}
	
	/*
	 * Utility function
	 * Calculates the moon phase from 0 to 7, following the order
	 * that we have stated above in the code.
	 */
	
	int computeMoonPhase(int year,int month,int day)
	{
	    
	    int g, e;
	
	    if (month == 1) --day;
	    else if (month == 2) day += 30;
	    else // m >= 3
	    {
	        day += 28 + (month-2)*3059/100;
	
	        // adjust for leap years
	        if ((year & 3) == 0) 
	        {
	        	++day;
	        }
	        if ((year%100) == 0)
	        {
	        	--day;
	        }
	        
	    }
	   
	    g = (year-1900)%19 + 1;
	    e = (11*g + 18) % 30;
	    if ((e == 25 && g > 11) || e == 24) e++;
	    return ((((e + day)*6+11)%177)/22 & 7);
	}

}


