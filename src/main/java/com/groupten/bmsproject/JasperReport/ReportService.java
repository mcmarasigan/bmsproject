package com.groupten.bmsproject.JasperReport;

import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Ingredient.IngredientRepository;
import com.groupten.bmsproject.ProductionSchedule.ProductionIngredientRepository;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleRepository;
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
    private ProductionScheduleRepository productionScheduleRepository;

    @Autowired
    private ProductionIngredientRepository productioningredientRepository;

    @Autowired
    private AdminService adminService;

    public String exportReport(String reportTemplate, String filePath) throws FileNotFoundException, JRException {
        List<?> data;
        String reportType;
        if (reportTemplate.contains("Ingredient")) {
            data = ingredientRepository.findAll();
            reportType = "IngredientReport";
        } else if (reportTemplate.contains("Sales")) {
            data = salesRepository.findAll();
            reportType = "SalesReport";
        } else if (reportTemplate.contains("IngredientUsed")) {
            data = productioningredientRepository.findAll();
            reportType = "IngredientUsedReport";
        } else{
            data = productionScheduleRepository.findAll();
            reportType = "ProductionReport";
        }

        File file = ResourceUtils.getFile("classpath:ReportTemplate/" + reportTemplate);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        String uuid = UUID.randomUUID().toString();
        String reportId = uuid.substring(0, 5);
        String userEmail = adminService.getLoggedInUserEmail();

        Map<String, Object> map = new HashMap<>();
        map.put("REPORT_ID", reportId);
        map.put("userEmail", userEmail);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);

        String timestamp = new SimpleDateFormat("dd-MM-yyyy_hh-mm a").format(new Date()).replace(" ", "_");
        String fullFilePath = String.format("%s%s%s_%s.pdf", filePath, File.separator, reportType, timestamp);

        JasperExportManager.exportReportToPdfFile(jasperPrint, fullFilePath);
        return "Report generated at: " + fullFilePath;
    }
}
