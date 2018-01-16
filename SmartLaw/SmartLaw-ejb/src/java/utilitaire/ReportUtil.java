/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitaire;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ReportExportConfiguration;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimplePptxReportConfiguration;
import net.sf.jasperreports.view.JRViewer;
import static utilitaire.ReportUtil.ReportType.DOCX;
import static utilitaire.ReportUtil.ReportType.PDF;
import static utilitaire.ReportUtil.ReportType.XLSX;

/**
 *
 * @author Misaina
 */
public class ReportUtil {
    
//       public static String pathDocs="\\\\SERVEUR-PC\\law-app-repertories\\docs\\";
    public static String pathDocs="D:\\work\\smrhr\\lawapp\\docs\\";
//   public static String pathDocs="\\\\RABENANTOANDRO\\docs\\";
   // public static String pathDocs="\\\\USER-PC\\logicieldata\\docs\\";
    public void exportToDOCX(Map<String, Object> parameters, String pathReportName, String pathNewFile) throws Exception 
    {        
        JasperPrint jasperPrint = fillReport(parameters, pathReportName);           
        OutputStream out=new FileOutputStream(new File(pathNewFile+".docx"));
        exporter(jasperPrint, ReportType.DOCX, out);
    }
    public void exportToPDF(Map<String, Object> parameters, String pathReportName, String pathNewFile) throws Exception 
    {        
        JasperPrint jasperPrint = fillReport(parameters, pathReportName);           
        OutputStream out=new FileOutputStream(new File(pathNewFile+".pdf"));
        exporter(jasperPrint, ReportType.PDF, out);
    }
    public void exportToXLSX(Map<String, Object> parameters, String pathReportName, String pathNewFile) throws Exception 
    {        
        JasperPrint jasperPrint = fillReport(parameters, pathReportName);           
        OutputStream out=new FileOutputStream(new File(pathNewFile+".xlsx"));
        exporter(jasperPrint, ReportType.XLSX, out);
    }
    public JasperPrint fillReport(Map<String, Object> parameters, String pathReportName) throws Exception
    {
        JasperPrint jasperPrint = JasperFillManager.fillReport(pathReportName+".jasper", parameters, new JREmptyDataSource());   
        return jasperPrint;
    }
    
    public void showViewer(Map<String, Object> parameters, String pathReport) throws Exception
    {
        try
        {
            JasperPrint jasperPrint=fillReport(parameters, pathReport);
//            JasperPrintManager.printReport(jasperPrint, true);
            JRViewer jr= new JRViewer(jasperPrint);
            jr.setVisible(true);
            JFrame frame=new JFrame();
            frame.setSize(900, 700);
            frame.getContentPane().add(jr);
            frame.setVisible(true);
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }
    
    public void exporter(JasperPrint jasperPrint, ReportType reportType, OutputStream out) throws Exception 
    {
        Exporter jRExporter = null;
        ReportExportConfiguration configuration = null;
        switch (reportType) {
            case PDF:
                jRExporter = new JRPdfExporter();
                configuration = new SimplePdfReportConfiguration();
                System.out.println("==========> PDF  JRPdfExporter");
                break;

            case DOCX:
                jRExporter = new JRDocxExporter();
                configuration = new SimpleDocxReportConfiguration();
                break;

            case XLSX:
                jRExporter = new JRXlsxExporter();
                configuration = new SimplePptxReportConfiguration();
                break;
        }
        if (jRExporter != null) {
            System.out.println("jRExporter begin");
            jRExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            jRExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            jRExporter.setConfiguration(configuration);
            jRExporter.exportReport();
            System.out.println("jRExporter end");
        } else {
            throw new JRException("Une erreur est survenue lors de la generation de l'etat.");
        }
    }
    
    /**
     * télécharger un fichier
     *
     * @param parameters
     * @param response
     * @param pathReport : nom du fichier .jasper
     * @param fileName
     * @throws Exception
     */
    public void download(Map<String, Object> parameters, HttpServletResponse response, ReportType reportType, String pathReport, String fileName) throws Exception {
        try {
            response.reset();
            JasperPrint jasperPrint = fillReport(parameters, pathReport);
            switch (reportType) {
                case PDF:
                    byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".pdf");
                    response.getOutputStream().write(pdfBytes);
                case DOCX:
                    
            }
            //uncomment this line to make browser download the file

            response.getOutputStream().flush();
            response.getOutputStream().close();
            response.flushBuffer();
            
        } catch (Exception ex) {
            throw ex;
        }

    }
    public enum ReportType{
        PDF, DOCX, XLSX
    }
}
