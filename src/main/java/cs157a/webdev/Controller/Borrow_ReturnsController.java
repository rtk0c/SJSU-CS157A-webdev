package cs157a.webdev.Controller;
import cs157a.webdev.daoClasses.*;
import cs157a.webdev.model.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

//We need to  create a class that inherits from HttpServlet
// while doing so we overrides methods doGet, doPost

public class Borrow_ReturnsController extends HttpServlet {

    private final Borrow_ReturnsDAO brDAO = new Borrow_ReturnsDAO();
    private final BooksDAO bookDAO = new BooksDAO();
    private final MembersDAO membersDAO = new MembersDAO();

    // https://www.geeksforgeeks.org/servlet-form-data/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo) || "/borrow_returns".equals(pathInfo)) {
            String memberIdParam = request.getParameter("memberId");
            if(memberIdParam != null) {
                try{
                    int memberId = Integer.parseInt(memberIdParam);
                    PrintWriter out = response.getWriter();
                    htmlMainTable(out);
                    List<Borrow_Returns> memBR = brDAO.sortBRAsc(memberId);
                    htmlBRMainTable(out, memBR);
                }catch (SQLException e)
                {
                    System.out.println(e + "   -->   view_br");
                }
            }


        }else if ("/checkout".equals(pathInfo)) {
            String bookIdToEdit = request.getParameter("bookId");
            try {
                int bookId = Integer.parseInt(bookIdToEdit);
                Books book = bookDAO.getBookById(bookId); // Get book from Books table TODO... check join function
                if (book != null) {
                    PrintWriter out = response.getWriter();
                    htmlCheckoutBook(out, request, book); // Pass the Books object
                } else {
                    response.getWriter().println("Error: Book with ID " + bookId + " not found.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            try (PrintWriter out = response.getWriter()) {
                out.println("<h1>Not Found</h1><p>The requested resource was not found.</p>");
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String pathInfo = request.getPathInfo();

        if ("/checkout".equals(pathInfo)) {
            //String updateId = request.getParameter("bookId");

            int memberId = Integer.parseInt(request.getParameter("memberId"));

            //TODO... if memberID no exist dont add
            try {
                if (!membersDAO.checkMemberIdExists(memberId)) {
                    System.out.println("Member ID DOES not exist!");
                    PrintWriter out = response.getWriter();
                    out.println("<h1>Member ID DOES not exist!</h1>");
                    out.println("<a href=\"/books\">TRY AGAIN</a>\n");
                    //response.sendRedirect(request.getContextPath() + "/members");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }



            int bookId = Integer.parseInt(request.getParameter("bookId"));
            String title = request.getParameter("bookName");
            Date borrowDate = Date.valueOf(request.getParameter("borrowDate"));
            Date returnDate = Date.valueOf(request.getParameter("returnDate"));
            Date dueDate = Date.valueOf(request.getParameter("dueDate"));
            String borrowedBookStatus = request.getParameter("borrowedBookStatus");
            int availableCopies= Integer.parseInt(request.getParameter("availableCopies"));


            Borrow_Returns newBR = new Borrow_Returns();

            newBR.setMember_id(memberId);
            newBR.setBook_id(bookId);
            //newBR.setTitle(title);
            newBR.setBorrow_date(borrowDate);
            newBR.setReturn_date(returnDate);
            newBR.setDue_date(dueDate);
            newBR.setBorrowed_book_status(borrowedBookStatus);
            //newBR.setAvailable_copies(availableCopies);
            ;
            try {
                brDAO.insertBR(newBR);
                response.sendRedirect(request.getContextPath() + "/members");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
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
        out.println("  font-size: 18px; \n");
        out.println("  line-height: 25px;\n");
        out.println("  border-radius: 4px;\n");
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
        out.println("</div>\n");
        out.println("</div>");



        out.println("<table border=1><tr>" +
                "<th>Member ID</th>" +
                "<th>Title</th>" +
                "<th>Book ID</th>" +
                "<th>Borrow Return ID</th>" +
                "<th>Borrow Date</th>" +
                "<th>Return Date</th>" +
                "<th>Due Date</th>"    +
                "<th>Borrowed Book Status</th>");

    }


    public void htmlCheckoutBook(PrintWriter out, HttpServletRequest request, Books book) throws SQLException {
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

        out.println("<h2>Checkout _ Book</h2>\n");

        out.println("<form action='" + request.getContextPath() + "/borrow_returns/checkout' method='post'>");
        //out.println("label for=\"memberId\">First name:</label><br>");
        out.println("<input type='hidden' id='bookId' name='bookId' value='" + book.getBook_id() + "'>");

        out.println("<div>");
        out.println("<h3>Title</h3>");
        //out.println("label for=\"firstName\">First name:</label><br>");
        out.println("<input type='text' id='title' name='title' value='" + book.getTitle() + "'>");
        out.println("</div>");




        // TODO.... FUNCTION TO CHECK MEMBER_ID EXISTS
        // https://developer.mozilla.org/en-US/docs/Learn_web_development/Extensions/Forms/Form_validation
        out.println("<div>");
        out.println("<h3>Member Id</h3>");
        out.println("<input type='text' id='memberId' name='memberId' value='" + "'>");
        out.println("</div>");

        //out.println("label for=\"email\">email:</label><br>");
        ///



        out.println("<div>");
        out.println("<h3>Available Copies</h3>");
        out.println("<input type='text' id='availableCopies' name='availableCopies' value='" + book.getAvailable_copies() + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Borrow Date</h3>");
        out.println("<input type='text' id='borrowDate' name='borrowDate' value='" + new java.sql.Date(System.currentTimeMillis()) + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Due Date</h3>");
        out.println("<input type='text' id='returnDate' name='returnDate' value='" + new java.sql.Date(System.currentTimeMillis()) + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Due Date</h3>");

        // TODO... fix time for each date make return date hidden as a temp value???
        long dueVal = System.currentTimeMillis();
        int millisPerDay = 24 * 60 * 60 * 1000;
        int daysAdd = 30;
        out.println("<input type='text' id='dueDate' name='dueDate' value='" + new java.sql.Date( dueVal + ( daysAdd * millisPerDay)) + "'>");
        out.println("</div>");




        out.println("<div>");
        out.println("<h3>Borrowed Book Status</h3>");
        out.println("<input type='text' id='borrowedBookStatus' name='borrowedBookStatus' value='Checked OUT...'>");
        out.println("</div>");

        // add cancel to revert back to pages members
        out.println("<input type='submit' value='Add Checkout'>");

        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

    }

    private void htmlBRMainTable(PrintWriter out, List<Borrow_Returns> allBR) {

        if (allBR != null) {
            for (Borrow_Returns br  : allBR) {
                out.println("<tr>");

                out.println("<td>" + br.getMember_id() + "</td>");
                out.println("<td>" + br.getTitle() + "</td>");
                out.println("<td>" + br.getBook_id() + "</td>");
                out.println("<td>" + br.getBr_id() + "</td>");
                out.println("<td>" + br.getBorrow_date() + "</td>");
                out.println("<td>" + br.getReturn_date() + "</td>");
                out.println("<td>" + br.getDue_date() + "</td>");
                out.println("<td>" + br.getBorrowed_book_status() + "</td>");
                out.println("</tr>");

            }

        } else {
            System.out.println("<h1>No members books are found.</h1>");
        }

    }


}

/*

    ./mmc 1024 16

    Current PID = 88881, Current Time

    Physical memeroySize = 1024, Memory Bound Size = 16


    Selects D from DM or E

    Start of memory Deump and the stuff

    0x 00 00 00


    click m

    Memory Mapping Tble

    PID REq SIZE Actual Zie Offset BUfferPTR
     */


