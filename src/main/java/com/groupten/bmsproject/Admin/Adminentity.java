package com.groupten.bmsproject.Admin;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Adminentity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    private Integer id;
    private String name;
    private String email;

    public Integer getID(){
        return id;
    }

    public void setID(Integer id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

}
