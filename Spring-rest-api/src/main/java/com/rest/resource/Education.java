package com.rest.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Education {

//	@Id
//	private ObjectId _id;
	
	private Collection<Qualification> qualifications;
	
	private Map<String, Qualification> qmap = new HashMap<String, Qualification>();
	
	
	public Education() {
		this(new ArrayList<Qualification>());
	}
	
	public Education(Collection<Qualification> q) {
		this.qualifications = q;
	}
	
    @JsonAnySetter
    public void setAdditionalProperty(String name, Qualification q) {
    	q.setTitle(name);
    	qualifications.add(q);
    	qmap.put(name, q);
    }

    @JsonAnyGetter
    public Map<String, Qualification> getAdditionalProperty() {
        return qmap;
    }

    public void setQuaifications(Collection<Qualification> quaifications) {
        this.qualifications = quaifications;
    }
    
    
    @Override
	public String toString() {
		return "Education [q=" + qualifications + "]";
	}
	
}