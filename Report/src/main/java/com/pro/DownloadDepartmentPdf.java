package com.pro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DownloadDepartmentPDF")
public class DownloadDepartmentPdf extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
    private static final String DB_USER = "sumanraj";
    private static final String DB_PASS = "12345678";
    
    private static final String Select_Query = "Select * from Dept_List ";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Department_data.pdf");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            //Add title and line break
            document.add(new Paragraph("Department Data List", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(Chunk.NEWLINE);

            //create table with 7 columns
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            // Set custom column widths: ID column is smaller
            float[] columnWidths = {1.0f, 3.0f, 3.0f};
            table.setWidths(columnWidths);

            // Table headers
            String[] headers = {"ID", "DeptName", "DeptCode"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setNoWrap(true);  // Prevent wrapping
                table.addCell(cell);
            }

            // Fetch data
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(Select_Query)) {

                while (rs.next()) {
                    PdfPCell idCell = new PdfPCell(new Phrase(String.valueOf(rs.getInt("ID")))); //get value for the cell
                    idCell.setNoWrap(true);  //no wrap content
                    table.addCell(idCell);  //add value to the cell

                    PdfPCell nameCell = new PdfPCell(new Phrase(rs.getString("DeptName")));
                    nameCell.setNoWrap(true);
                    table.addCell(nameCell);

                    PdfPCell phoneCell = new PdfPCell(new Phrase(rs.getString("DeptCode")));
                    phoneCell.setNoWrap(true);
                    table.addCell(phoneCell);

                   
                }
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new ServletException("Exception in PDF creation: " + e.getMessage());
        }
    }
}