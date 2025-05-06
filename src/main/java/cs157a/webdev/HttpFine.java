package cs157a.webdev;

import cs157a.webdev.model.*;
import org.eclipse.jetty.server.*;

import java.util.*;

public class HttpFine extends BaseHttpHandler {
    @Override
    protected String handleGet(Request req) throws Exception {
        var params = getHtmlFormParams(req);


        // first step to get br by id
        // go through them find ones that are late, calculate fine, insert into fines >>> check inserted
        String memIdParam = params.getValue("memberId");
        int memId = Integer.parseInt(memIdParam);
        Db.borrows.checkFines(memId);
        List<Fines> memFin = Db.fines.sortFinesAsc(memId);
        //System.out.println("Fines: " + memFin);

        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_table_border
        // Use this to make tables recall header vs cell data
        // See example servet code 9.18
        // Make main bar to navigate to info

        return STR."""
            <!DOCTYPE html>
            <html lang="en">
            <body>
            <head>
            <title>Fines infomation</title>
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
                font-size: 18px;
                border-radius: 4px;
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
            </head>
            <body>
            <div class="header">
                <div class="header-right">
                    <a class="active" href="/">Home</a>
                </div>
            </div>
            <table>
                <tr>
                    <th>BR ID</th>
                    <th>Fine Total</th>
                    <th>Fine Status</th>
                    <th>Update Fine Info</th>

                </tr>
                \{htmlBRMainTable(memFin, memId)}
            </table>
            """;
    }

    //added book checkout column




    private String htmlBRMainTable(List<Fines> memFin, int memberId) {
        if (memFin == null) {
            return "<h1>No members books are found.</h1>";
        }

        var sb = new StringBuilder();

        for (Fines fin :  memFin) {
            sb.append(STR."""
                <tr>

                <td>\{fin.getBr_id()}</td>
                <td>\{fin.getFine_total()}</td>
                <td>\{fin.getFine_status()}</td>

                <td>
                <form action="/fines/update" method="get">
                    <input type="hidden" name="brId" value="\{fin.getBr_id()}">
                    <input type="hidden" name="memberId" value="\{memberId}">
                    <button type="submit">Update Fines Fees</button>
                </form>
                </td>

                </tr>""");
        }
        return sb.toString();
    }
}
