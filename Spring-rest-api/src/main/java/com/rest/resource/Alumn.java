package com.rest.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class Alumn implements Serializable {

	private static final long serialVersionUID = -4807537037772269496L;

	private static final Logger log = LoggerFactory.getLogger(Alumn.class);
	
	@Id
	@JsonIgnore
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
	}
	
	@JsonIgnore
	public ObjectId getObjectId() {
		return _id;
	}
	
	public String getId() {
		if (_id != null) {
			return _id.toString();
		}
		return "-1";
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
	
	public boolean isValid() {
		/**
		 * Valid if
		 * 	Country is not empty && contains only letters 
		 * 	Street is not empty && contains only letters
		 * 	Number not empty && contains only digits
		 */
		
		return this.getAddresses()
				.stream()
				.anyMatch(
					t -> !t.getCountry().isEmpty() || t.getCountry().chars().allMatch(x -> Character.isLetter(x) || Character.isSpaceChar(x)) || 
					!t.getStreet().isEmpty() || t.getStreet().chars().allMatch(x -> Character.isLetter(x) || Character.isSpaceChar(x)) ||
					!t.getNumber().isEmpty() || t.getNumber().chars().allMatch(x -> Character.isDigit(x))
					);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Alumn) {
			final Alumn other = (Alumn) obj;
			return Objects.equals(this.getId().toString(), other.getId().toString());
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		if (this._id != null) {
			return Objects.hashCode(this._id.toString());
		}
		
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "Alumn [_id=" + _id + ", name=" + name + ", addresses=" + addresses + ", education=" + education + "]";
	}
	
	
}