package com.interview.moonphasesapi;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
//Use default API methods from Repository
public interface MoonPhasesRepository extends CrudRepository<MoonPhases, Long> {

}
