package com.project.app.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@SuppressWarnings("deprecation")
@Component
public class ReportsUtil {
	private static final Logger logger = LogManager.getLogger(ReportsUtil.class);
	
    private static DataSource dataSource; 
	
	@Autowired
	private DataSource dataSource_;

	@PostConstruct     
	private void initStaticDataSource () {
		dataSource = this.dataSource_;
	}
	
	public static ResponseEntity<byte[]> exportPDF(Map<String, Object> parameters, String reportName, HttpServletResponse response) throws JRException, IOException, SQLException {    	
    	try {
	    	final JasperReport report = loadTemplate(reportName);
	        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource.getConnection());            
	        return ResponseEntity.ok()
	          		 .header("Content-Type", "application/pdf; charset=UTF-8")
	          		 .header("Content-Disposition", "inline; filename=expPDF.pdf")
	          		 .body(JasperExportManager.exportReportToPdf(jasperPrint));
	        
    	}catch (Exception e) {
    		logger.info(String.format("Error pdf export : %s", e.getMessage()));
			return null;
		}
    }
    
    public static ResponseEntity<byte[]> exportXLS(Map<String, Object> parameters, String reportName, HttpServletResponse response) throws JRException, IOException, SQLException {    	
    	try {
	    	final JasperReport report = loadTemplate(reportName);
	        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource.getConnection());  
	        JRXlsExporter exporterXLS = new JRXlsExporter();
	        ByteArrayOutputStream  xlsReport = new ByteArrayOutputStream();
	        exporterXLS.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	        exporterXLS.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			exporterXLS.getReportContext();  
			exporterXLS.exportReport();			
			return ResponseEntity.ok()
	           		 .header("Content-Type", "application/vnd.ms-excel; charset=UTF-8")
	           		 .header("Content-Disposition", "inline; filename=expXLS.xls")
	           		 .body(xlsReport.toByteArray());
			
    	}catch (Exception e) {
    		logger.info(String.format("Error xls export : %s", e.getMessage()));
			return null;
		}
    }
    
    public static ResponseEntity<byte[]> exportXLSX(Map<String, Object> parameters, String reportName, HttpServletResponse response) throws JRException, IOException, SQLException {    	
    	try {
	    	final JasperReport report = loadTemplate(reportName);
	        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource.getConnection());  
	        JRXlsxExporter exporterXLSX = new JRXlsxExporter();
	        ByteArrayOutputStream  xlsxReport = new ByteArrayOutputStream();
	        exporterXLSX.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	        exporterXLSX.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsxReport);
	        exporterXLSX.getReportContext();  
	        exporterXLSX.exportReport();
			return ResponseEntity.ok()
	           		 .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8")
	           		.header("Content-Disposition", "inline; filename=expXLSX.xlsx")
	           		 .body(xlsxReport.toByteArray());
    	}catch (Exception e) {
    		logger.info(String.format("Error xlsx export : %s", e.getMessage()));
			return null;
		}
    }
    
    
    public static ResponseEntity<byte[]> exportDOCX(Map<String, Object> parameters, String reportName, HttpServletResponse response) throws JRException, IOException, SQLException {    	
    	try {
	    	final JasperReport report = loadTemplate(reportName);
	        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource.getConnection());  
	        JRDocxExporter exporterDocx = new JRDocxExporter ();
	        ByteArrayOutputStream  docxReport = new ByteArrayOutputStream();
	        exporterDocx.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	        exporterDocx.setParameter(JRExporterParameter.OUTPUT_STREAM, docxReport);
	        exporterDocx.getReportContext();  
	        exporterDocx.exportReport();
			return ResponseEntity.ok()
	           		 .header("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document; charset=UTF-8")
	           		.header("Content-Disposition", "inline; filename=expDOCX.docx")
	           		 .body(docxReport.toByteArray());
			
    	}catch (Exception e) {
    		logger.info(String.format("Error docx export : %s", e.getMessage()));
			return null;
		}
    }

	static JasperReport loadTemplate(String reportName) throws JRException, FileNotFoundException, UnsupportedEncodingException {
    	String path = "/reports/"+reportName+".jrxml";
    	logger.info(String.format("Invoice template path : %s", path));
        final InputStream reportInputStream = ReportsUtil.class.getClass().getResourceAsStream(path);
        final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);
        return JasperCompileManager.compileReport(jasperDesign);
    }
}