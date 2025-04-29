package com.Controller;

import com.daoClasses.MembersDAO;
import com.model.Members;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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

                PrintWriter out = response.getWriter();
                htmlMainTable(out);

                //List <Members> allMembers = membersDAO.selectAllMembers();
                List < Members > allMembers = membersDAO.sortMembersAsc();
                htmlListMembers(out, allMembers);

            } catch (Exception e) // check if valid
            {
                System.out.println(e);
            }

        } else if ("/add".equals(pathInfo)) {
            //String html = readFile("view/add-member-form.html");

            try (PrintWriter out = response.getWriter()) {
                htmlAddMember(out, request);
                //out.println(html);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("/update".equals(pathInfo)) {
            String memberIdToEdit = request.getParameter("memberId");
            try {
                int memberId = Integer.parseInt(memberIdToEdit);
                Members member = membersDAO.getMemberById(memberId);
                if (member != null) {

                    PrintWriter out = response.getWriter();
                    htmlUpdate(out, request, member);

                } else {
                    response.getWriter().println("Error: Member with ID " + memberId + " not found for editing.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            try (PrintWriter out = response.getWriter()) {
                out.println("<h1>Not Found</h1><p>The requested resource was not found.</p>");
            }
        }
    }

  /*private String readFile(String filename) {
      InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filename);
      Scanner scanner = new Scanner(resourceAsStream);
      StringBuilder content = new StringBuilder();
      while (scanner.hasNextLine()) {
          content.append(scanner.nextLine());
      }
      return content.toString();
  }*/

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
                    System.out.println("Member not found");
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
                    System.out.println("ERROR in update Post");
                    //response.sendRedirect(request.getContextPath() + "/members");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    // moved html code to own section was getting cluttered
    // consider own html files instead?

    public void htmlUpdate(PrintWriter out, HttpServletRequest request, Members member) {
        // both ID and Name have same value -> getElementById
        // Can implement this as a script later for JS
        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_form_submit
        // This example came in handy since it showed with prefilled attributes
        // needed since we are reusing add methods for updating
        // find out about labels being printed
        // TODO... INPUT VALIDATION
        // css make ->> https://www.w3schools.com/css/tryit.asp?filename=trycss_forms

        out.println("<!DOCTYPE html>");
        out.println("<html><body>");

        out.println("<head>");

        out.println("<style>\n");
        out.println("input[type=text], select {\n");
        out.println("  width: 100%;\n");
        out.println("  padding: 12px 20px;\n");
        out.println("  margin: 2px 0;\n");
        out.println("  display: inline-block;\n");
        out.println("  border: 1px solid #ccc;\n");
        out.println("  border-radius: 4px;\n");
        out.println("  box-sizing: border-box;\n");
        out.println("}");

        out.println("input[type=submit] {");
        out.println("  width: 100%;");
        out.println("  background-color: #00ffcc;");
        out.println("  color: BLACK;");
        out.println("  padding: 14px 20px;");
        out.println("  margin: 8px 0;");
        out.println("  border: none;");
        out.println("  border-radius: 4px;");
        out.println("  cursor: pointer;");
        out.println("}");

        out.println("input[type=submit]:hover {");
        out.println("  background-color: #1b727f;\n");
        out.println("}");

        out.println("div {");
        out.println("  border-radius: 5px;\n");
        out.println("  background-color: #f2f2f2;\n");
        out.println("  padding: 20px;");
        out.println("}");

        out.println("</style>\n");
        out.println("</head>");
        out.println("<body>");


        ///out.println("</head>");

        out.println("<h2>Update User</h2>\n");

        out.println("<form action='" + request.getContextPath() + "/members/update' method='post'>");
        //out.println("label for=\"memberId\">First name:</label><br>");
        out.println("<input type='hidden' id='memberId' name='memberId' value='" + member.getMember_id() + "'>");

        out.println("<div>");
        out.println("<h3>First Name</h3>");

        //out.println("label for=\"firstName\">First name:</label><br>");
        out.println("<input type='text' id='firstName' name='firstName' value='" + member.getFirst_name() + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Last Name</h3>");
        out.println("<input type='text' id='lastName' name='lastName' value='" + member.getLast_name() + "'>");
        out.println("</div>");

        //out.println("label for=\"email\">email:</label><br>");
        out.println("<div>");
        out.println("<h3>Email</h3>");
        out.println("<input type='text' id='email' name='email' value='" + member.getEmail() + "'>");
        out.println("</div>");

        // add cancel to revert back to pages members
        out.println("<input type='submit' value='Update'>");

        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

    }

    private void htmlMainTable(PrintWriter out) {

        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_table_border
        // Use this to make tables recall header vs cell data
        // See example servet code 9.18
        // Make main bar to navigate to info

        out.println("<html><body>");
        out.println("<head>");


        out.println("<style>");


        out.println("table {\n");
        out.println("  margin-left: auto; \n");
        out.println("  margin-right: auto;");
        out.println("  width: 90%;");
        out.println("  max-width: 1000px;");
        out.println("}");

        out.println("th, td {");
        out.println("  text-align: center;");
        out.println("  padding: 8px;");
        out.println("  border-bottom: 1px solid #ddd;");
        out.println("}");

        out.println("tr:nth-child(even){background-color: #f2f2f2}\n");
        out.println("th {");
        out.println("  background-color: #00ffcc;");
        out.println("  color: BLACK;");
        out.println("}");


        // header
        out.println(".header {");
        out.println("  overflow: hidden;\n");
        out.println("  background-color: #f1f1f1;\n");
        out.println("  padding: 20px 10px;\n");

        out.println("}");

        out.println(".header a {\n");
        out.println("  float: left;\n");
        out.println("  color: blue;\n");
        out.println("  text-align: center;\n");
        out.println("  padding: 12px;\n");
        out.println("  text-decoration: none;\n");
        out.println("  font-size: 16px; \n");
        out.println("  line-height: 20px;\n");
        out.println("  border-radius: 10px;\n");
        out.println("}");


        out.println(".header a.active {");
        out.println("background-color: dodgerblue;");
        out.println("color: white;");
        out.println("}");


        out.println("@media screen and (max-width: 500px) {\n");
        out.println("  .header a {\n");
        out.println("    float: none;\n");
        out.println("    display: flex;\n");
        out.println("    text-align: left;\n");
        out.println("}");


        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class=\"header\">\n");
        out.println("    <div class=\"header-right\">\n");
        out.println("    <a class=\"active\" href=\"/\">Home</a>\n");
        out.println("    <a  href=\"/members/add\">Add Member</a>\n");
        out.println("</div>\n");
        out.println("</div>");


        // TODO... Style and implent script to search by title?
        out.println("<input type=\"text\" id=\"myInput\" onkeyup=\"myFunction()\" placeholder=\"Search for names..\" title=\"Type in a name\">");


        out.println("<table border=1><tr>" +
                "<th>Member ID</th>" +
                "<th>First Name</th>" +
                "<th>Last Name</th>" +
                "<th>Email INFO</th>" +
                "<th>Date Registered</th>" +
                "<th>EDIT</th>" +
                "<th>DELETE</th>");

    }

    private void htmlListMembers(PrintWriter out, List < Members > allMembers) {

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
                // /members/update fixed but still check
                out.println("<form action=/members/update method=get>");
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
            System.out.println("<h1>No members found.</h1>");
        }

    }

    public void htmlAddMember(PrintWriter out, HttpServletRequest request) {
        // both ID and Name have same value -> getElementById
        // Can implement this as a script later for JS
        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_form_submit
        // This example came in handy since it showed with prefilled attributes
        // needed since we are reusing add methods for updating
        // find out about labels being printed
        // TODO... INPUT VALIDATION
        // css make ->> https://www.w3schools.com/css/tryit.asp?filename=trycss_forms

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");

        out.println("<style>\n");
        out.println("input[type=text], select {\n");
        out.println("  width: 100%;\n");
        out.println("  padding: 12px 20px;\n");
        out.println("  margin: 2px 0;\n");
        out.println("  display: inline-block;\n");
        out.println("  border: 1px solid #ccc;\n");
        out.println("  border-radius: 4px;\n");
        out.println("  box-sizing: border-box;\n");
        out.println("}");

        out.println("input[type=submit] {");
        out.println("  width: 100%;");
        out.println("  background-color: #00ffcc;");
        out.println("  color: BLACK;");
        out.println("  padding: 14px 20px;");
        out.println("  margin: 8px 0;");
        out.println("  border: none;");
        out.println("  border-radius: 4px;");
        out.println("  cursor: pointer;");
        out.println("}");

        out.println("input[type=submit]:hover {");
        out.println("  background-color: #1b727f;\n");
        out.println("}");

        out.println("div {");
        out.println("  border-radius: 5px;\n");
        out.println("  background-color: #f2f2f2;\n");
        out.println("  padding: 20px;");
        out.println("}");

        out.println("</style>\n");
        out.println("</head>");
        out.println("<body>");

        out.println("<h2>Add User</h2>\n");

        out.println("<form action='" + request.getContextPath() + "/members/add' method='post'>");
        //out.println("label for=\"memberId\">First name:</label><br>");
        out.println("<input type='hidden' id='memberId' name='memberId' value='" + "'>");

        out.println("<div>");
        out.println("<h3>First Name</h3>");

        //out.println("label for=\"firstName\">First name:</label><br>");
        out.println("<input type='text' id='firstName' name='firstName' value='" + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Last Name</h3>");
        out.println("<input type='text' id='lastName' name='lastName' value='" + "'>");
        out.println("</div>");

        //out.println("label for=\"email\">email:</label><br>");
        out.println("<div>");
        out.println("<h3>Email</h3>");
        out.println("<input type='text' id='email' name='email' value='" + "'>");
        out.println("</div>");

        // add cancel to revert back to pages members
        out.println("<input type='submit' value='Add MEMBER'>");

        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

    }

}
