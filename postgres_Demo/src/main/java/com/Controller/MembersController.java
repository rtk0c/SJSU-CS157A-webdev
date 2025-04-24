package com.Controller;

import com.daoClasses.MembersDAO;
import com.model.Members;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

//We need to  create a class that inherits from HttpServlet
// while doing so we overrides methods doGet, doPost

public class MembersController extends HttpServlet {

    private final MembersDAO membersDAO = new MembersDAO();

    // https://www.geeksforgeeks.org/servlet-form-data/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            try {

                // Provided sample code
                /*public void showLoginForm(HttpServletRequest req, HttpServletResponse res) throws IOException {
                    res.setContentType("text/html");
                    PrintWriter out = res.getWriter();
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Login</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<br>Please enter username and password");
                    out.println("<form method=post>");
                    out.println("<br>Username: <input type=text name=username>");
                    out.println("<br>Password: <input type=text name=password>");
                    out.println("<br><input type=submit>");
                    out.println("</form>");
                    out.println("</body>");
                    out.println("</html>");
                }
                */




                // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_table_border
                // Use this to make tables recall header vs cell data
                // See example servet code 9.18
                // Make main bar to navigate to info
                PrintWriter out = response.getWriter();
                out.println("<html><body>");
                out.println("<h3>Member Details</h3>");
                out.println("<table border=1><tr>" +
                        "<th>Member ID</th>" +
                        "<th>First Name</th>" +
                        "<th>Last Name</th>" +
                        "<th>Email INFO</th>" +
                        "<th>Date Registered</th>" +
                        "<th>EDIT</th>" +
                        "<th>DELETE</th>");

                        //"<th>View Borrowed Books</th>" +
                        //"<th>View Member Fines</th>" +

                List <Members> allMembers = membersDAO.selectAllMembers();
                membersDAO.selectAllMembers();
                if (allMembers != null) {
                    for (Members member: allMembers) {
                        out.println("<tr>");

                        out.println("<td>" + member.getMember_id() + "</td>");
                        out.println("<td>" + member.getFirst_name() + "</td>");
                        out.println("<td>" + member.getLast_name() + "</td>");
                        out.println("<td>" + member.getEmail() + "</td>");
                        out.println("<td>" + member.getMembership_date() + "</td>");

                        // TODO... check why only works for http://localhost:9999/members vs /members/
                        out.println("<td>");
                        out.println("<form action=members/update method=get>");
                        out.println("<input type=hidden name=memberId value=" + member.getMember_id() + ">");
                        out.println("<button type=submit>Update</button>");
                        out.println("</form>");
                        out.println("</td>");

                        out.println("<td>");
                        out.println("<form action=members/delete method=post>");
                        out.println("<input type=hidden name=memberId value=" + member.getMember_id() + ">");
                        out.println("<button type=submit>Delete</button>");

                        out.println("</form>");
                        out.println("</td>");


                        // generate remaining columns (view member fines, borrowed books, etc)

                        out.println("  </tr>");
                        //out.close();

                    }
                } else {
                    out.println("<h1>No members found.</h1>");
                }

            } catch (Exception e) // check if valid
            {
                out.println(e);
            }

        } else if ("/add".equals(pathInfo)) {
            String html = readFile("view/add-member-form.html");
            try (PrintWriter out = response.getWriter()) {
                out.println(html);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("/update".equals(pathInfo)) {
            // TODO... post complete since using same html file with slight changes
            // https://stackoverflow.com/questions/4055199/html-how-to-pre-populate-form-field-with-known-value-upon-load






        }else {
                out.println("<h1>Not Found</h1><p>The resource was not found.</p>");
        }
    }

    private String readFile(String filename) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filename);
        Scanner scanner = new Scanner(resourceAsStream);
        StringBuilder content = new StringBuilder();
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine());
        }
        return content.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String pathInfo = request.getPathInfo();

        if ("/add".equals(pathInfo)) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            Members newMember = new Members();
            newMember.setFirst_name(firstName);
            newMember.setLast_name(lastName);
            newMember.setEmail(email);
            newMember.setMembership_date(Date.valueOf(LocalDate.now()));

            try {
                membersDAO.insertMember(newMember);
                response.sendRedirect(request.getContextPath() + "/members");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else if (("/delete".equals(pathInfo))) {
            String memRemove = request.getParameter("memberId");
            try {
                int memberId = Integer.parseInt(memRemove);
                boolean deleted = membersDAO.deleteMember(memberId);
                if (deleted) {
                    response.sendRedirect(request.getContextPath() + "/members");
                } else {
                    // for debugging right now
                    out.println("Member not found");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

          // relatively similar to //add
        } else if (("/update".equals(pathInfo))) {
            String updateId = request.getParameter("memberId");

            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            try {
                int memId = Integer.parseInt(updateId);
                Members updatedMember = new Members();

                updatedMember.setFirst_name(firstName);
                updatedMember.setLast_name(lastName);
                updatedMember.setEmail(email);
                updatedMember.setMember_id(memId);
                boolean updated = membersDAO.updateMember(updatedMember);
                if (updated) {
                    response.sendRedirect(request.getContextPath() + "/members");
                } else {
                    out.println("ERROR in update Post");
                    //response.sendRedirect(request.getContextPath() + "/members");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

}