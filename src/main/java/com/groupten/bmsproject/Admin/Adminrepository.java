package com.groupten.bmsproject.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Adminrepository extends JpaRepository<Adminentity, Integer> {
    Adminentity findByEmail(String email);
    Adminentity findByUsername(String username); // Add this method
}
