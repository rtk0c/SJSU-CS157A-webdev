package cs157a.webdev;

import cs157a.webdev.model.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.*;

import java.sql.*;

/**
 * Members directory
 */
public class HttpMembers extends BaseHttpHandler {
    @Override
    protected String handleGet(Request req) throws Exception {
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
            <script>
            function deleteThisMember(member) {
              fetch(`/member?memberId=${member.dataset.memberid}`, {
                method: 'DELETE',
              })
              alert('Member deleted. Refresh page to see the updated book list.')
            }
            </script>
            </head><body>
            <div class="header">
                <div class="header-right">
                <a class="active" href="/">Home</a>
                <a href="/member/">Add Member</a>
            </div>
            </div>
                <table>
                    <tr><th>Member ID</th><th>First Name</th><th>Last Name</th><th>Email INFO</th><th>Date Registered</th><th>Checkout Books</th><th>View Fines</th><th>EDIT</th><th>DELETE</th></tr>
                    \{htmlListMembers()}
                </table>
            </div>
            </body></html>
            """;
    }

    // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_table_border
    // Use this to make tables recall header vs cell data
    // See example servet code 9.18
    // Make main bar to navigate to info

    private String htmlListMembers() throws SQLException {
        var sb = new StringBuilder();
        for (Members member : Db.members.sortMembersAsc()) {
            //language=html
            sb.append(STR."""
                <tr>
                <td>\{member.getMember_id()}</td>
                <td>\{member.getFirst_name()}</td>
                <td>\{member.getLast_name()}</td>
                <td>\{member.getEmail()}</td>
                <td>\{member.getMembership_date()}</td>
                <td>
                    <form action="/borrows" method="get">
                          <input type="hidden" name="memberId" value="\{member.getMember_id()}">
                          <button type="submit">View Checkedout Books</button>
                          </form>
                </td>
                <td>
                    <form action="/fines" method="get">
                        <input type="hidden" name="memberId" value="\{member.getMember_id()}">
                        <button type="submit">View Fines</button>
                        </form>
                </td>
                <td>
                <form action="/member" method="get">
                    <input type="hidden" name="memberId" value="\{member.getMember_id()}">
                    <button type="submit">Update</button>
                </form>
                </td>
                <td>
                <button onclick='deleteThisMember(this)' data-memberid='\{member.getMember_id()}'>Delete</button>
                </td>
                </tr>
             """);
        }
        return sb.toString();
    }
}