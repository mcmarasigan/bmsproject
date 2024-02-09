package com.groupten.bmsproject.Admin;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Adminentity {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "admin_seq")
    @SequenceGenerator(name = "admin_seq", sequenceName = "admin_sequence", allocationSize = 1)
    
    private Integer id;
    private String username;
    private String email;

    public Integer getID(){
        return id;
    }

    public void setID(Integer id){
        this.id = id;
    }

    public String getuserName(){
        return username;
    }

    public void setuserName(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

}
