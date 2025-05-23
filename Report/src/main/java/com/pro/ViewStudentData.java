package com.pro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ViewStudentData")
public class ViewStudentData extends HttpServlet {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
    private static final String DB_USER = "sumanraj";
    private static final String DB_PASS = "12345678";
    
    private static final String View_Query = "SELECT * FROM Registered_Student_List";
    private static final String Update_Query = "UPDATE Registered_Student_List SET ID=?,StudentName=?, PhoneNo=?, Native=?, Email=?, DOB=?, Department=? WHERE ID=?";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Student List</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("<style>");
        
        out.println("<style>");
        out.println("body { background-color: #ccebff; font-size: 0.60rem; margin: 0; padding: 0; }");
        out.println(".card-container { background-color: white; padding: 0.1rem; border-radius: 1px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); width: 90%; margin: 1cm auto; }");

        out.println(".table-container { max-height: 80vh; overflow-y: auto; overflow-x: auto; }");
        out.println("table { font-size: 1rem; table-layout: auto; width: 30%; }"); // allow flexible column width
        out.println("th, td { padding: 0.1rem; vertical-align: middle; white-space: nowrap; }"); // don't wrap text
        out.println("thead th { position: sticky; top: 0; background-color: #212529; color: white; z-index: 1; }");
        out.println("</style>");
        
        
       
        out.println("<div>");
        out.println("<br>");       
        out.println("<a href='Index.jsp' class='btn btn-outline-success fw-bold'>Home</a>");
        out.println("<a href='RegisterStudent.jsp' class='btn btn-outline-primary fw-bold'>Register Student</a>");
//        out.println("<a href='Report' class='btn btn-outline-warning fw-bold'>Filter Student Data</a>");
        out.println("</div>");
        out.println("<br");

        Connection con = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            ps = con.prepareStatement(View_Query);

            ResultSet rs = ps.executeQuery();

            out.println("<div class='table-container'>");
            out.println("<table class='table table-bordered table-striped table-hover align-middle text-center'>");
            out.println("<thead class='table-dark'><tr>");
            out.println("<th>ID</th>");
            out.println("<th>Student Name</th>");
            out.println("<th>Phone No</th>");
            out.println("<th>Native</th>");
            out.println("<th>Email</th>");
            out.println("<th style='min-width: 150px;'>DOB</th>");
            out.println("<th>Department</th>");
            out.println("<th>Edit</th>");
            out.println("<th>Delete</th>");
            out.println("</tr></thead><tbody>");
            
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt(1) + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<td>" + rs.getString(4) + "</td>");
                out.println("<td>" + rs.getString(5) + "</td>");
                out.println("<td>" + rs.getDate(6) + "</td>");
                out.println("<td>" + rs.getString(7) + "</td>");

                // Edit
                out.println("<td>");
                out.println("<form action='UpdataStudent.jsp' method='get' onsubmit='return confirm(\"Are you sure you want to edit?\")'>");

                out.println("<input type='hidden' name='id' value='" + rs.getInt("ID") + "'/>");
                out.println("<input type='hidden' name='name' value='" + rs.getString("StudentName") + "'/>");
                out.println("<input type='hidden' name='phone' value='" + rs.getString("PhoneNo") + "'/>");
                out.println("<input type='hidden' name='nativeplace' value='" + rs.getString("Native") + "'/>");
                out.println("<input type='hidden' name='email' value='" + rs.getString("Email") + "'/>");
                out.println("<input type='hidden' name='dob' value='" + rs.getString("DOB") + "'/>");
                out.println("<input type='hidden' name='dept' value='" + rs.getString("Department") + "'/>");

                out.println("<button type='submit' class='btn btn-warning btn-sm fw-bold'>Edit</button>");
                out.println("</form>");
                out.println("</td>");

                // Delete
                out.println("<td>");
                out.println("<form action='DeleteStudent' method='post' onsubmit='return confirm(\"Are you sure you want to delete?\")'>");
                out.println("<input type='hidden' name='id' value='" + rs.getInt(1) + "'/>");
                out.println("<button type='submit' class='btn btn-danger btn-sm fw-bold'>Delete</button>");
                out.println("</form>");
                
                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</tbody></table></div>");  // Close table-responsive
            out.println("</div>"); // Close card-container

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<div class='alert alert-danger text-center'>Error: " + e.getMessage() + "</div>");
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            out.println("<a href='DownloadStudentPDF' class='btn btn-outline-danger fw-bold'>Download PDF</a>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    }
    }
    