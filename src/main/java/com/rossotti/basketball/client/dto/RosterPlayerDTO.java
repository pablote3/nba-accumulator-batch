package com.rossotti.basketball.client.dto;


import org.joda.time.LocalDate;

public class RosterPlayerDTO {
	private String last_name;
	private String first_name;
	private String display_name;
	private LocalDate birthdate;
	private String birthplace;
	private Short height_in;
	private Short weight_lb;
	private String position;
	private String uniform_number;
    
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public LocalDate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}
	public Short getHeight_in() {
		return height_in;
	}
	public void setHeight_in(Short height_in) {
		this.height_in = height_in;
	}
	public Short getWeight_lb() {
		return weight_lb;
	}
	public void setWeight_lb(Short weight_lb) {
		this.weight_lb = weight_lb;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getUniform_number() {
		return uniform_number;
	}
	public void setUniform_number(String uniform_number) {
		this.uniform_number = uniform_number;
	}
}
