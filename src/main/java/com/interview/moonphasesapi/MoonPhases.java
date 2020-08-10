package com.interview.moonphasesapi;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Properties and methods for MoonPhases Model")
public class MoonPhases {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@ApiModelProperty(notes="Unique id for the MoonPhases Object.")
	public long id;
	@ApiModelProperty(notes="Month of a given year for the MoonPhases Object. Example of a correct format: January")
	public String month;
	@ApiModelProperty(notes="Year for the MoonPhases Object. Example of a correct format: 2004")
	public Integer year;
	@ElementCollection
	@ApiModelProperty(notes="Map for the MoonPhases Object that contains key-value pairs as: {moon phase:day month}")
	public Map<String, String> moonPhasesYearMonth = new HashMap<>();
	
	
	public MoonPhases() {
		this.month = "";
		this.year = null;
		this.moonPhasesYearMonth = null;
	}
	
	public MoonPhases(String month, int year, Map<String, String> moonPhases) {
		this.month = month;
		this.year = year;
		this.moonPhasesYearMonth = moonPhases;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public Map<String, String> getMoonPhasesYearMonth() {
		return moonPhasesYearMonth;
	}
	public void setMoonPhasesYearMonth(Map<String, String> moonPhasesYearMonth) {
		this.moonPhasesYearMonth = moonPhasesYearMonth;
	}
	
	@Override
	public String toString() {
		return "MoonPhases [id=" + id + ", month=" + month + ", year=" + year + "]";
	}
	

}
