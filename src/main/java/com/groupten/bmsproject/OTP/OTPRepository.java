package com.groupten.bmsproject.OTP;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends CrudRepository <OTPEntity, String> {
    
}
