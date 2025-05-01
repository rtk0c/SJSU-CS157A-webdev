package cs157a.webdev;

import cs157a.webdev.daoClasses.*;
import cs157a.webdev.model.*;
import org.eclipse.jetty.http.*;
import org.eclipse.jetty.io.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.*;

import java.sql.*;
import java.time.*;

/**
 * Members directory for each function (add, list, update etc whatever we need)
 */
public class HttpMembers extends Handler.Abstract {

    private final MembersDAO membersDAO = new MembersDAO();

    @Override
    public boolean handle(Request request, Response response, Callback callback) throws Exception {
        return switch (request.getMethod()) {
            case "POST" -> handlePost(request, response, callback);
            case "GET" -> handleGet(request, response, callback);
            default -> false;
        };
    }

    private boolean handleGet(Request request, Response response, Callback callback) throws Exception {
        var uri = request.getHttpURI();
        var path = uri.getPath();
        MultiMap<String> params = UrlEncoded.decodeQuery(uri.getQuery());

        var body = switch (path) {
            case "/members/" -> htmlMainTable();
            case "/members/add/" -> htmlAddMember(params);
            case "/members/update/" -> htmlUpdate(params);
            default -> null;
        };

        if (body == null) {
            // TODO proper 404 page
            return false;
        }

        response.setStatus(200);
        response.getHeaders().put(HttpHeader.CONTENT_TYPE, "text/html; charset=UTF-8");
        Content.Sink.write(response, false, body, callback);
        return true;
    }

    private boolean handlePost(Request request, Response response, Callback callback) throws Exception {
        var uri = request.getHttpURI();
        var path = uri.getPath();
        MultiMap<String> params = UrlEncoded.decodeQuery(uri.getQuery());

        switch (path) {
            case "/add" -> {
                String firstName = params.getValue("firstName", 0);
                String lastName = params.getValue("lastName", 0);
                String email = params.getValue("email", 0);

                Members newMember = new Members();
                newMember.setFirst_name(firstName);
                newMember.setLast_name(lastName);
                newMember.setEmail(email);
                newMember.setMembership_date(Date.valueOf(LocalDate.now()));

                membersDAO.insertMember(newMember);
                Response.sendRedirect(request, response, callback, "/members");
                return true;
            }
            case "/delete" -> {
                String memRemove = params.getValue("memberId", 0);

                int memberId = Integer.parseInt(memRemove);
                boolean deleted = membersDAO.deleteMember(memberId);
                if (deleted) {
                    Response.sendRedirect(request, response, callback, "/members");
                    return true;
                } else {
                    // for debugging right now
                    System.out.println("Member not found");
                    return false;
                }

                // relatively similar to //add
            }
            case "/update" -> {
                String updateId = params.getValue("memberId", 0);

                String firstName = params.getValue("firstName", 0);
                String lastName = params.getValue("lastName", 0);
                String email = params.getValue("email", 0);

                int memId = Integer.parseInt(updateId);
                Members updatedMember = new Members();

                updatedMember.setFirst_name(firstName);
                updatedMember.setLast_name(lastName);
                updatedMember.setEmail(email);
                updatedMember.setMember_id(memId);
                boolean updated = membersDAO.updateMember(updatedMember);
                if (updated) {
                    Response.sendRedirect(request, response, callback, "/members");
                    return true;
                } else {
                    System.out.println("ERROR in update Post");
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
    }

    public String htmlUpdate(MultiMap<String> params) throws SQLException {
        var memberIdToEdit = params.getValue("memberId", 0);
        int memberId = Integer.parseInt(memberIdToEdit);
        Members member = membersDAO.getMemberById(memberId);
        if (member == null) {
            return "Error: Member with ID " + memberId + " not found for editing.";
        }

        // both ID and Name have same value -> getElementById
        // Can implement this as a script later for JS
        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_form_submit
        // This example came in handy since it showed with prefilled attributes
        // needed since we are reusing add methods for updating
        // find out about labels being printed
        // TODO... INPUT VALIDATION
        // css make ->> https://www.w3schools.com/css/tryit.asp?filename=trycss_forms

        //language=html
        return STR."""
            <!DOCTYPE html>
            <html lang='en'><body><head>
            <title>Update member | Library Management</title>
            <style>
            input[type=text], select {
              width: 100%;
              padding: 12px 20px;
              margin: 2px 0;
              display: inline-block;
              border: 1px solid #ccc;
              border-radius: 4px;
              box-sizing: border-box;
            }
            input[type=submit] {
              width: 100%;
              background-color: #00ffcc;
              color: BLACK;
              padding: 14px 20px;
              margin: 8px 0;
              border: none;
              border-radius: 4px;
              cursor: pointer;
            }
            input[type=submit]:hover {
              background-color: #1b727f;
            }
            div {
              border-radius: 5px;
              background-color: #f2f2f2;
              padding: 20px;
            }
            </style>
            </head><body>
            <h2>Update User</h2>
            <form action='/members/update' method='post'>
                <input type='hidden' id='memberId' name='memberId' value='\{member.getMember_id()}'>
                <!-- <label for="memberId">First name:</label><br> -->
                <div>
                    <h3>First Name</h3>
                    <input type='text' id='firstName' name='firstName' value='\{member.getFirst_name()}'>
                    <!-- <label for="firstName">First name:</label><br> -->
                </div>
                <div>
                    <h3>Last Name</h3>
                    <input type='text' id='lastName' name='lastName' value='\{member.getLast_name()}'>
                </div>
                <div>
                    <h3>Email</h3>
                    <input type='text' id='email' name='email' value='\{member.getEmail()}'>
                    <!-- <label for="email">email:</label><br> -->
                </div>
                <!-- add cancel to revert back to pages members -->
                <input type='submit' value='Update'>
            </form>
            </body></html>
            """;

    }

    // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_table_border
    // Use this to make tables recall header vs cell data
    // See example servet code 9.18
    // Make main bar to navigate to info
    private String htmlMainTable() throws SQLException {
        //language=HTML
        return STR."""
            <!DOCTYPE html>
            <html lang="en"><head>
            <title>Members | Library Management</title>
            <style>
            table {
              margin-left: auto;
              margin-right: auto;
              width: 90%;
              max-width: 1000px;
            }
            th, td {
              text-align: center;
              padding: 8px;
              border-bottom: 1px solid #ddd;
            }
            tr:nth-child(even){ background-color: #f2f2f2 }
            th {
              background-color: #00ffcc;
              color: BLACK;
            }
            .header {
              overflow: hidden;
              background-color: #f1f1f1;
              padding: 20px 10px;
            }
            .header a {
              float: left;
              color: blue;
              text-align: center;
              padding: 12px;
              text-decoration: none;
              font-size: 16px;
              line-height: 20px;
              border-radius: 10px;
            }
            .header a.active {
              background-color: dodgerblue;
              color: white;
            }
            @media screen and (max-width: 500px) {
              .header a {
                float: none;
                display: flex;
                text-align: left;
              }
            }
            </style>
            </head><body>
            <div class="header">
                <div class="header-right">
                <a class="active" href="/">Home</a>
                <a href="/members/add">Add Member</a>
            </div>
            </div>
                <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for names.." title="Type in a name">
                <table>
                    <tr><th>Member ID</th><th>First Name</th><th>Last Name</th><th>Email INFO</th><th>Date Registered</th><th>EDIT</th><th>DELETE</th></tr>
                    \{htmlListMembers()}
                </table>
            </div>
            </body></html>
            """;
    }

    private String htmlListMembers() throws SQLException {
        var sb = new StringBuilder();
        for (Members member : membersDAO.sortMembersAsc()) {
            //language=html
            // TODO... check why only works for http://localhost:9999/members vs /members/
            sb.append(STR."""
                <tr>
                <td>\{member.getMember_id()}</td>
                <td>\{member.getFirst_name()}</td>
                <td>\{member.getLast_name()}</td>
                <td>\{member.getEmail()}</td>
                <td>\{member.getMembership_date()}</td>
                <td>
                <form action=/members/update method=get>
                    <input type=hidden name=memberId value=\{member.getMember_id()}>
                    <button type=submit>Update</button>
                </form>
                </td>
                <td>
                <form action=members/delete method=post>
                    <input type=hidden name=memberId value=\{member.getMember_id()}>
                    <button type=submit>Delete</button>
                </form>
                </td>
                <!-- TODO generate remaining columns (view member fines, borrowed books, etc) -->
                </tr>
             """);
        }
        return sb.toString();
    }

    public String htmlAddMember(MultiMap<String> params) {
        // both ID and Name have same value -> getElementById
        // Can implement this as a script later for JS
        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_form_submit
        // This example came in handy since it showed with prefilled attributes
        // needed since we are reusing add methods for updating
        // find out about labels being printed
        // TODO... INPUT VALIDATION
        // css make ->> https://www.w3schools.com/css/tryit.asp?filename=trycss_forms

        //language=html
        return """
            <!DOCTYPE html>
            <html lang="en"><head>
            <title>Add member | Library Management</title>
            <style>
            input[type=text], select {
              width: 100%;
              padding: 12px 20px;
              margin: 2px 0;
              display: inline-block;
              border: 1px solid #ccc;
              border-radius: 4px;
              box-sizing: border-box;
            }
            input[type=submit] {
              width: 100%;
              background-color: #00ffcc;
              color: BLACK;
              padding: 14px 20px;
              margin: 8px 0;
              border: none;
              border-radius: 4px;
              cursor: pointer;
            }
            input[type=submit]:hover {
              background-color: #1b727f;
            }
            div {
              border-radius: 5px;
              background-color: #f2f2f2;
              padding: 20px;
            }
            </style>
            </head><body>
            <h2>Add User</h2>
            <form action='/members/add' method='post'>
                <!-- <label for="memberId">First name:</label><br> -->
                <input type='hidden' id='memberId' name='memberId' value='" + "'>
                <div>
                    <h3>First Name</h3>
                    <!-- <label for="firstName">First name:</label><br> -->
                    <input type='text' id='firstName' name='firstName' value='" + "'>
                </div>
                <div>
                    <h3>Last Name</h3>
                    <input type='text' id='lastName' name='lastName' value='" + "'>
                </div>
                <div>
                    <h3>Email</h3>
                    <!-- <label for="email">email:</label><br> -->
                    <input type='text' id='email' name='email' value='" + "'>
                </div>
                <!-- add cancel to revert back to pages members -->
                <input type='submit' value='Add MEMBER'>
            </form>
            </body>
            </html>
            """;
    }
}
