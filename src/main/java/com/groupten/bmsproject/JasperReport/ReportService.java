package com.groupten.bmsproject.JasperReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Ingredient.IngredientRepository;
import com.groupten.bmsproject.Sales.SalesEntity;
import com.groupten.bmsproject.Sales.SalesRepository;
import com.groupten.bmsproject.SecurityLogs.SecurityLogs;
import com.groupten.bmsproject.SecurityLogs.SecurityLogsRepository;
import com.groupten.bmsproject.Admin.AdminService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = "C:\\Users\\Maria\\3rd Year Projects\\softeng\\bmsproject\\src\\main\\java\\com\\groupten\\bmsproject\\Report Files";

        List<IngredientEntity> ingredients = ingredientRepository.findAll();

        // Load file and compile
        File file = ResourceUtils.getFile("classpath:ReportTemplate/Ingredient_Report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ingredients);

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

        if (reportFormat.equalsIgnoreCase("pdf")) {
            // Format the current date and time
            String timestamp = new SimpleDateFormat("dd-MM-yyyy_hh-mm a").format(new Date());
            String fileName = String.format("%s\\IngredientReport_%s.pdf", path, timestamp);
            JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
            return "report generated in path: " + fileName;
        }
        return "Unsupported report format: " + reportFormat;
    }

    public String exportSalesReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = "C:\\Users\\Maria\\3rd Year Projects\\softeng\\bmsproject\\src\\main\\java\\com\\groupten\\bmsproject\\Report Files";

        List<SalesEntity> sales = salesRepository.findAll();

        // Load file and compile
        File file = ResourceUtils.getFile("classpath:ReportTemplate/Sales_Report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(sales);

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

        if (reportFormat.equalsIgnoreCase("pdf")) {
            // Format the current date and time
            String timestamp = new SimpleDateFormat("dd-MM-yyyy_hh-mm a").format(new Date());
            String fileName = String.format("%s\\SalesReport_%s.pdf", path, timestamp);
            JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
            return "report generated in path: " + fileName;
        }
        return "Unsupported report format: " + reportFormat;
    }

    public String exportSecurityLogReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = "C:\\Users\\Maria\\3rd Year Projects\\softeng\\bmsproject\\src\\main\\java\\com\\groupten\\bmsproject\\Report Files";

        List<SecurityLogs> securityLogs = securitylogsRepository.findAll();

        // Load file and compile
        File file = ResourceUtils.getFile("classpath:ReportTemplate/Security_Report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(securityLogs);

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

        if (reportFormat.equalsIgnoreCase("pdf")) {
            // Format the current date and time
            String timestamp = new SimpleDateFormat("dd-MM-yyyy_hh-mm a").format(new Date());
            String fileName = String.format("%s\\SecurityLogReport_%s.pdf", path, timestamp);
            JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
            return "report generated in path: " + fileName;
        }
        return "Unsupported report format: " + reportFormat;
    }
}
