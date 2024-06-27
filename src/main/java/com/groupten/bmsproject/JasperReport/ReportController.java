package com.groupten.bmsproject.JasperReport;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import net.sf.jasperreports.engine.JRException;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;
    
   @GetMapping("/report/{format}")
    public String generateReport(@PathVariable String format) throws FileNotFoundException, JRException {
        return reportService.exportReport(format);
    }
}
