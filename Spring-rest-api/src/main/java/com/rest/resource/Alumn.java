package com.rest.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Alumn implements Serializable {

	private static final long serialVersionUID = -4807537037772269496L;

	private static final Logger log = LoggerFactory.getLogger(Alumn.class);
	
	@Id
	private ObjectId _id;
	
	@Indexed
	private String name;
	
	private Collection<Address> addresses;
	
	private Education education;
	
	
	public Alumn() {
		this("", new ArrayList<Address>(), new Education());
	}
	
	public Alumn(String name, Collection<Address> addresses, Education education) {
		this.name = name;
		this.addresses = addresses;
		this.education = education;
//		this._id = ObjectId.get();
	}
	
	
	public ObjectId getId() {
		return _id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Collection<Address> addresses) {
		this.addresses = addresses;
	}

	public Education getEducation() {
		return education;
	}

	public void setEducation(Education education) {
		this.education = education;
	}
	
	@Override
	public String toString() {
		return "Alumn [name=" + name + ", addresses=" + addresses + ", education=" + education + "]";
	}
}