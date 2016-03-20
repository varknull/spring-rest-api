package com.rest.resource;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class Qualification {

//	@Id
//	private ObjectId _id;

	@JsonIgnore
	private String title;
	
	private String university;
	private int year;

	
	public Qualification() {
		this("",0);
	}
	
	public Qualification(String university, int year) {
		this.university = university;
		this.year = year;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "Education [university=" + university + ", year=" + year + "]";
	}
}
