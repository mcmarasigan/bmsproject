package com.groupten.bmsproject.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Adminrepository extends CrudRepository<Adminentity, Integer> {
    
}
