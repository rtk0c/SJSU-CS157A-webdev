package cs157a.webdev;

import cs157a.webdev.model.*;
import org.eclipse.jetty.server.*;

import java.sql.*;
import java.time.*;

public class HttpMember extends BaseHttpHandler {
    @Override
    protected String handleGet(Request req) throws Exception {
        var params = getHtmlFormParams(req);

        Members member;
        String memberIdField;

        var memberId = params.getValue("memberId");
        if (memberId == null) {
            // Insert
            member = new Members(); // load dummy values
            memberIdField = "";
        } else {
            // Update
            member = Db.members.getMemberById(Integer.parseInt(memberId));
            memberIdField = STR."<input type='hidden' name='memberId' value='\{member.getMember_id()}'>";
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
            <form action='/member' method='post'>
                \{memberIdField}
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

    @Override
    protected String handlePost(Request req) throws Exception {
        var params = getHtmlFormParams(req);

        String updateId = params.getValue("memberId");

        String firstName = params.getValue("firstName");
        String lastName = params.getValue("lastName");
        String email = params.getValue("email");

        Members newMember = new Members();
        newMember.setFirst_name(firstName);
        newMember.setLast_name(lastName);
        newMember.setEmail(email);
        newMember.setMembership_date(Date.valueOf(LocalDate.now()));

        if (updateId == null) {
            Db.members.insertMember(newMember);
        } else {
            newMember.setMember_id(Integer.parseInt(updateId));
            boolean updated = Db.members.updateMember(newMember);
            if (!updated) {
                throw new RuntimeException("ERROR in update Post");
            }
        }

        responseType = HTTP_REDIRECT;
        return "/members";
    }

    @Override
    protected String handleDelete(Request req) throws Exception {
        var params = getHtmlFormParams(req);

        String memRemove = params.getValue("memberId");

        boolean deleted = Db.members.deleteMember(Integer.parseInt(memRemove));
        if (!deleted) {
            // for debugging right now
            System.out.println("ERROR in delete Post");
            throw new RuntimeException("Member not found");
        }

        responseType = HTTP_REDIRECT;
        return "/members";

        // relatively similar to //add

    }
}
