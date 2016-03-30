package com.rest.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.base.Strings;

@RestController
public class AlumniController {
	private static final Logger log = LoggerFactory.getLogger(AlumniController.class);
	private static final int PAGE_SIZE = 5;
	
	@Autowired
	private AlumnRepository alumnRepo;


	/**
	 * Possible way to handle api versioning 
	 * 
	 * @RequestMapping(headers = {
	    "Accept=application/vnd.company.app-1.0+json",
	    "Accept=application/vnd.company.app-1.1+json"
		})
	 * 	
	 */
	@RequestMapping(value = "/alumni", method = RequestMethod.GET)
    public Response get(@RequestParam(value="name", required = false, defaultValue = "") String name,
    		@RequestParam(value="edu", required = false, defaultValue = "") String edu,
    		@RequestParam(value="page", required = false, defaultValue = "1") int pageNum ) {

    	Response res = new Response();
    	Page<Alumn> page = null;
    	
    	PageRequest pageable = new PageRequest(pageNum - 1, PAGE_SIZE);   
    	
    	if (!Strings.isNullOrEmpty(name)) {
    		log.info("name: "+ name);
    		
    		
    		if (!Strings.isNullOrEmpty(edu)) {
    			page = alumnRepo.findByNameAndEducation(name, edu, pageable).orElseThrow(
        				() -> new ResourceNotFoundException(edu));
    		} else {
    		
    			page = alumnRepo.findByName(name, pageable).orElseThrow(
	    				() -> new ResourceNotFoundException(name));
    		}
    		
    		res.setData(page);
    		
    	} else {
    		if (!Strings.isNullOrEmpty(edu)) {
    			page = alumnRepo.findByEducation(edu, pageable).orElseThrow(
        				() -> new ResourceNotFoundException(edu));
    		} else {
    			page = alumnRepo.findAll(pageable);
    		}
    	}
    	res.setData(page);
        
        return res;
    }
    
    @RequestMapping(value = "/alumni/{id}", method = RequestMethod.GET)
    public Alumn findOne(@PathVariable String id) {
      return alumnRepo.findOne(id);
    }
    
    @RequestMapping(value = "/alumni/{id}", method = RequestMethod.DELETE)
    public void deleteOne(@PathVariable String id) {
    	alumnRepo.delete(id);
    }
    
	@RequestMapping(value = "/alumni", method = RequestMethod.POST)
	public ResponseEntity<?> add(@RequestBody Alumn input) {
		if (!input.isValid()) {
			throw new NotValidException();
		}
		
		log.debug(input.toString());
		Alumn result = alumnRepo.save(input);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getId()).toUri());
		
		httpHeaders.set("id", result.getId());
		
		return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
		
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	class NotValidException extends RuntimeException {

		public NotValidException() {
			super("Bad request");
		}
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	class ResourceNotFoundException extends RuntimeException {

		public ResourceNotFoundException(String name) {
			super("Could not find user '" + name + "'.");
		}
	}
	
}