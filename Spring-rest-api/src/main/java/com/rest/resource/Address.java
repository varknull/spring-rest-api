package com.rest.resource;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address {

//	@Id
//	private ObjectId _id;
	
	private String street;
	private String number;
	private String country;
	
	public Address() {
		this("", "", "");
	}
	
	public Address(String street, String num, String country) {
		this.street = street;
		this.number = num;
		this.country = country;
	}
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@Override
	public String toString() {
		return "Address [street=" + street + ", number=" + number + ", country=" + country + "]";
	}
}