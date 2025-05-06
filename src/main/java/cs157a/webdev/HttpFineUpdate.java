package cs157a.webdev;

import cs157a.webdev.model.*;
import org.eclipse.jetty.server.*;

import java.sql.*;
import java.time.*;

public class HttpFineUpdate extends BaseHttpHandler {
    @Override
    protected String handleGet(Request req) throws Exception {
        var params = getHtmlFormParams(req);

        Fines fine;
        String brIdField;

        var brId = params.getValue("brId");
        var memberId = params.getValue("memberId");

        if (brId == null) {
            // Insert
            fine = new Fines(); // load dummy values
            brIdField = "";
        } else {
            // Update
            fine = Db.fines.getFineById(Integer.parseInt(brId));
            brIdField = STR."<input type='hidden' name='brId' value='\{fine.getBr_id()}'>";
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
            <title>Update Fines | Library Management</title>
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
            <h2>Update Fine</h2>
            <form action='/fines/update' method='post'>
                \{brIdField}
                <!-- <label for="memberId">First name:</label><br> -->
                <div>

                    <input type='hidden' id='memberId' name='memberId' value='\{memberId}'>
                    <!-- <label for="firstName">First name:</label><br> -->
                </div>
                <div>
                    <h3>BR ID</h3>
                    <input type='text' id='brId' name='brId' value='\{fine.getBr_id()}'>
                    <!-- <label for="firstName">First name:</label><br> -->
                </div>
                <div>
                    <h3>Fine Total</h3>
                    <input type='text' id='fineTotal' name='fineTotal' value='\{fine.getFine_total()}'>
                </div>
                <div>
                    <h3>Fine Status</h3>
                    <input type='text' id='fineStatus' name='fineStatus' value='\{fine.getFine_status()}'>
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

        String updateId = params.getValue("brId");
        String memberId = params.getValue("memberId");

        int fineTotal = Integer.parseInt(params.getValue("fineTotal"));
        boolean fineStatus = Boolean.parseBoolean((params.getValue("fineStatus")));

        Fines newFine = new Fines();
        newFine.setFine_total(fineTotal);
        newFine.setFine_status(fineStatus);

        if (updateId == null) {
            Db.fines.insertFine(newFine);
        } else {
            newFine.setBr_id(Integer.parseInt(updateId));
            boolean updated = Db.fines.updateFine(newFine);
            if (!updated) {
                throw new RuntimeException("ERROR in update Post");
            }
        }

        responseType = HTTP_REDIRECT;
        return "http://localhost:9999/fines?memberId=" + memberId; // why path didn't work or my browser is just busted :)
    }

}
