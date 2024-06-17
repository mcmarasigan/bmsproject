package com.groupten.bmsproject.Admin;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Adminentity {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "admin_seq")
    @SequenceGenerator(name = "admin_seq", sequenceName = "admin_sequence", allocationSize = 1)
    
    private Integer id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;

    public Integer getID(){
        return id;
    }

    public String getfirstName() {
        return firstname;
    }

    public void setfirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getlastName() {
        return lastname;
    }

    public void setlastName(String lastname) {
        this.lastname = lastname;
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

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
