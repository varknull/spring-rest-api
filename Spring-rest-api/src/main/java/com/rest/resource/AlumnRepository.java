package com.rest.resource;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnRepository extends MongoRepository<Alumn, String> {

	/* Non paged queries */
    public Optional<Collection<Alumn>> findByName(String name);
    
    @Query(value = "{ 'education.qualifications.title' : ?0 }")
    public Optional<Collection<Alumn>> findByEducation(String edu);

    @Query(value = "{ 'name' : ?0 ,'education.qualifications.title' : ?1 }")
    public Optional<Collection<Alumn>> findByNameAndEducation(String name, String edu);
    
    
    /* Paged queries */
    public Optional<Page<Alumn>> findByName(String name, Pageable pageable);
    
    @Query(value = "{ 'education.qualifications.title' : ?0 }")
    public Optional<Page<Alumn>> findByEducation(String edu, Pageable pageable);

    @Query(value = "{ 'name' : ?0 ,'education.qualifications.title' : ?1 }")
    public Optional<Page<Alumn>> findByNameAndEducation(String name, String edu, Pageable pageable);


}