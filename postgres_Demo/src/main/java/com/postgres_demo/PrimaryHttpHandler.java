package com.postgres_demo;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpHeader;

import java.io.IOException;
import java.io.PrintWriter;

public class PrimaryHttpHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(200);

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Jetty Hello World Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>Hello World1 (Servlet)</p>");
            // temp for easy access
            out.println("<a href=\"http://localhost:9999/members/\">MEMBERS</a>\n");
            out.println("<a href=\"http://localhost:9999/books/\">BOOKS</a>\n");


            out.println("</body>");
            out.println("</html>");
        }
    }
}
