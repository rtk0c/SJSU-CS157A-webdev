package cs157a.webdev;

import cs157a.webdev.model.*;
import org.eclipse.jetty.server.*;

import java.sql.*;

public class HttpCheckout extends BaseHttpHandler {
    @Override
    protected String handleGet(Request req) throws Exception {
        var params = getHtmlFormParams(req);

        String bookIdToEdit = params.getValue("bookId");
        int bookId = Integer.parseInt(bookIdToEdit);
        Books book = Db.books.getBookById(bookId); // Get book from Books table TODO... check join function
        if (book != null) {
            return htmlCheckoutBook(book); // Pass the Books object
        } else {
            return "Error: Book with ID " + bookId + " not found.";
        }
    }

    @Override
    protected String handlePost(Request req) throws Exception {
        var params = getHtmlFormParams(req);

        //String updateId = params.getValue("bookId");

        int memberId = Integer.parseInt(params.getValue("memberId"));

        //TODO... if memberID no exist dont add
        if (!Db.members.checkMemberIdExists(memberId)) {
            return """
                <h1> Member ID DOES not exist!</h1>
                <a href="/books">TRY AGAIN</a>
                """;
        }

        int bookId = Integer.parseInt(params.getValue("bookId"));
        Date borrowDate = Date.valueOf(params.getValue("borrowDate"));
        Date returnDate = Date.valueOf(params.getValue("returnDate"));
        Date dueDate = Date.valueOf(params.getValue("dueDate"));

        Borrow_Returns newBR = new Borrow_Returns();
        newBR.setMember_id(memberId);
        newBR.setBook_id(bookId);
        newBR.setBorrow_date(borrowDate);
        newBR.setReturn_date(returnDate);
        newBR.setDue_date(dueDate);
        //newBR.setBorrowed_book_status(borrowedBookStatus);

        Books book = Db.books.getBookById(bookId); // Update books value after we checked out books
        if (Db.books.updateBooksAvailable(book)) {
            Db.borrows.insertBR(newBR);
            responseType = HTTP_REDIRECT;
            return "/members";
        } else {
            // On fail go to our error page for case 0 available
            //language=html
            return """
                <!DOCTYPE html>
                <html>
                <head>
                <title>Fail Page</title>
                </head>
                <body>
                    <p>Book add fail page</p>
                    <a href="/books">No More Books Available Try again/another by clicking here :)))</a>
                </body>
                </html>
                """;
        }

    }

    public String htmlCheckoutBook(Books book) {
        // both ID and Name have same value -> getElementById
        // Can implement this as a script later for JS
        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_form_submit
        // This example came in handy since it showed with prefilled attributes
        // needed since we are reusing add methods for updating
        // find out about labels being printed
        // TODO... INPUT VALIDATION
        // css make ->> https://www.w3schools.com/css/tryit.asp?filename=trycss_forms

        // TODO... fix time for each date make return date hidden as a temp value???
        long dueVal = System.currentTimeMillis();
        int millisPerDay = 24 * 60 * 60 * 1000;
        int daysAdd = 30;
        var now = new Date(System.currentTimeMillis());

        //language=html
        return STR."""
            <!DOCTYPE html>
            <html lang="en">
            <head>
            <title>Checkout | Library Management</title>
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
                width: 100 %;
                background-color: #00ffcc;
                color: BLACK;
                padding 14px 20px;
                margin: 8px 0;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            input[type = submit]:hover {
                background-color: #1b727f;
            }
            </style>
            </head>
            <body>
            <h2>Checkout Book</h2>
            <form action='/borrows/checkout' method='post'>
            <input type='hidden' name='bookId' value = '\{book.getBook_id()}' >
            <label>
                Title
                <input type='text' name='title' value = '\{book.getTitle()}' >
            </label>
            <!-- TODO.... FUNCTION TO CHECK MEMBER_ID EXISTS -->
            <!-- https://developer.mozilla.org/en-US/docs/Learn_web_development/Extensions/Forms/Form_validation -->
            <label>
                Member Id
                <input type='text' name='memberId' value = '' >
            </label>
            <label>
                Available Copies
                <input type='text' name='availableCopies' value = '\{book.getAvailable_copies()}' >
            </label>
            <label>
                Borrow Date
                <input type='text' name='borrowDate' value = '\{now}' >
            </label>
            <label>
                Return Date
                <input type='text' name='returnDate' value = '\{now}' >
            </label>
            <label>
                Due Date
                <input type='text' name='dueDate' value = '\{new Date(dueVal + ((long) daysAdd * millisPerDay))}' >
            </label>

            <!-- add cancel to revert back to pages members -->
            <input type = 'submit' value = 'Add Checkout' >
            </form>
            </body>
            </html>
            """;
    }

}
