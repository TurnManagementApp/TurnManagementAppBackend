package edu.uptc.swii.shiftmgmt.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uptc.swii.shiftmgmt.domain.model.Shift;

public interface ShiftRepository extends MongoRepository<Shift, Integer> {
    
}
