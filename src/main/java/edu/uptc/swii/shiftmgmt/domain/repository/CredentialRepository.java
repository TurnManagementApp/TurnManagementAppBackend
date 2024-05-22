package edu.uptc.swii.shiftmgmt.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uptc.swii.shiftmgmt.domain.model.Credentials;

public interface CredentialRepository extends JpaRepository<Credentials, Integer>{
    
}
