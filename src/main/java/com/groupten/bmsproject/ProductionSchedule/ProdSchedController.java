package com.groupten.bmsproject.ProductionSchedule;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ProdSchedController {
    
    @Autowired
    private ProductionScheduleService productionScheduleService;

    @PostMapping(path = "/addProductSched")
    public String addnewProductSched(@RequestParam String productname, @RequestParam Double quantity, @RequestParam String lvlofstock, @RequestParam LocalDate dateofproduction, @RequestParam LocalDate expdate, @RequestParam Integer numberofdaysexp) {
        return productionScheduleService.addnewProductSched(productname, quantity, lvlofstock, dateofproduction, expdate, numberofdaysexp);
    }
}
