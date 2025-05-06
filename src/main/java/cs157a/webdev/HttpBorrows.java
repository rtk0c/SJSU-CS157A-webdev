package cs157a.webdev;

import cs157a.webdev.model.*;
import org.eclipse.jetty.server.*;

import java.util.*;

public class HttpBorrows extends BaseHttpHandler {
    @Override
    protected String handleGet(Request req) throws Exception {
        var params = getHtmlFormParams(req);

        String memberIdParam = params.getValue("memberId");
        int memberId = Integer.parseInt(memberIdParam);
        List<Borrow_Returns> memBR = Db.borrows.sortBRAsc(memberId);

        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_table_border
        // Use this to make tables recall header vs cell data
        // See example servet code 9.18
        // Make main bar to navigate to info

        return STR."""
            <!DOCTYPE html>
            <html lang="en">
            <body>
            <head>
            <title>Borrws & Returns | Library management</title>
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
                    <th>Member ID</th>
                    <th>Book ID</th>
                    <th>Borrow Date</th>
                    <th>Return Date</th>
                    <th>Due Date</th>
                </tr>
                \{htmlBRMainTable(memBR)}
            </table>
            """;
    }

    //added book checkout column




    private String htmlBRMainTable(List<Borrow_Returns> allBR) {
        if (allBR == null) {
            return "<h1>No members books are found.</h1>";
        }

        var sb = new StringBuilder();
        for (Borrow_Returns br : allBR) {
            sb.append(STR."""
                <tr>

                <td>\{br.getMember_id()}</td>
                <td>\{br.getBook_id()}</td>
                <td>\{br.getBorrow_date()}</td>
                <td>\{br.getReturn_date()}</td>
                <td>\{br.getDue_date()}</td>


                </tr>""");
        }
        return sb.toString();
    }
}
