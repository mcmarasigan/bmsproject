package com.groupten.bmsproject.JasperReport;

import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Ingredient.IngredientRepository;
import com.groupten.bmsproject.Sales.SalesEntity;
import com.groupten.bmsproject.Sales.SalesRepository;
import com.groupten.bmsproject.SecurityLogs.SecurityLogs;
import com.groupten.bmsproject.SecurityLogs.SecurityLogsRepository;
import com.groupten.bmsproject.Admin.AdminService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private SecurityLogsRepository securitylogsRepository;

    @Autowired
    private AdminService adminService;

    public String exportReport(String reportTemplate, String filePath) throws FileNotFoundException, JRException {
        // Fetch the appropriate data based on the report template
        List<?> data;
        if (reportTemplate.contains("Ingredient")) {
            data = ingredientRepository.findAll();
        } else if (reportTemplate.contains("Sales")) {
            data = salesRepository.findAll();
        } else {
            data = securitylogsRepository.findAll();
        }

        // Load file and compile
        File file = ResourceUtils.getFile("classpath:ReportTemplate/" + reportTemplate);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        // Generate a UUID
        String uuid = UUID.randomUUID().toString();
        // Take the first 5 characters as the report ID
        String reportId = uuid.substring(0, 5);

        // Get the logged-in user's email
        String userEmail = adminService.getLoggedInUserEmail();

        Map<String, Object> map = new HashMap<>();
        map.put("REPORT_ID", reportId);
        map.put("userEmail", userEmail); // Add user email parameter

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);

        // Export the report to the specified path
        JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
        return "Report generated at: " + filePath;
    }
}
