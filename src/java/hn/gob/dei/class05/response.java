/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.gob.dei.class05;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Arias
 */
public class response extends HttpServlet {

    @Resource(lookup = "jdbc/employee")
    private DataSource dataSource;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            String name = request.getParameter("name");
            if (name != null && name.length() > 0) {
                
                try (Connection connection = dataSource.getConnection()) {
                    Statement statement = connection.createStatement();
                    String sql = String.format("SELECT * FROM employees WHERE first_name LIKE \"%s%%\"", name);
                    ResultSet resultSet = statement.executeQuery(sql);
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>First Name</th>");
                    out.println("<th>Last Name</th>");
                    out.println("<th>Birth Date</th>");
                    out.println("<th>Gender</th>");
                    out.println("<th>Hire Date</th>");
                    out.println("</tr>");
                    while (resultSet.next()) {
                        out.println("<tr>");
                        out.printf("<td>%s</td>%n", resultSet.getString("first_name"));
                        out.printf("<td>%s</td>%n", resultSet.getString("last_name"));
                        out.printf("<td>%s</td>%n", resultSet.getString("birth_date"));
                        out.printf("<td>%s</td>%n", resultSet.getString("gender"));
                        out.printf("<td>%s</td>%n", resultSet.getString("hire_date"));
                        out.println("</tr>");
                    }
                    out.println("</table>");
                } catch (SQLException sqlex) {
                    out.println("Excepcion de SQL");
                    out.println(sqlex.getMessage());
                    for (StackTraceElement element: sqlex.getStackTrace())
                        out.println(element.toString() + "</br>");
                }

            }
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Responde el query SELECT * FROM employees WHERE first_name LIKE param%";
    }

}
