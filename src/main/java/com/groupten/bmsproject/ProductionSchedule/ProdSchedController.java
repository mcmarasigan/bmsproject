package com.groupten.bmsproject.ProductionSchedule;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ProdSchedController {
    
    @Autowired
    private ProductionScheduleService productionScheduleService;

    @PostMapping(path = "/addProductSched")
    public String addnewProductSched(@RequestParam String productname, @RequestParam Integer quantity, @RequestParam String lvlofstock, @RequestParam LocalDateTime dateofproduction, @RequestParam LocalDateTime expdate, @RequestParam Integer numberofdaysexp) {
        return productionScheduleService.addnewProductSched(productname, quantity, lvlofstock, dateofproduction, expdate, numberofdaysexp);
    }
}
