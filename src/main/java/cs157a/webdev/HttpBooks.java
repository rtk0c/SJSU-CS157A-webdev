package cs157a.webdev;

import cs157a.webdev.daoClasses.*;
import cs157a.webdev.model.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

//We need to  create a class that inherits from HttpServlet
// while doing so we overrides methods doGet, doPost

// TODO(rtk0c): finish conversion
public class HttpBooks extends HttpServlet {

    private final BooksDAO booksDAO = new BooksDAO();

    // https://www.geeksforgeeks.org/servlet-form-data/
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        String pathInfo = request.getPathInfo();

        switch (pathInfo) {
            case "/" -> {
                try {
                    PrintWriter out = response.getWriter();
                    htmlMainTable(out);

                    //List <Members> allMembers = membersDAO.selectAllMembers();
                    List<Books> allBooks = booksDAO.sortBooksAsc(); //TODO...
                    htmlListMembers(out, allBooks);

                } catch (Exception e) // check if valid
                {
                    System.out.println();
                }
            }
            case "/add" -> {
                //String html = readFile("view/add-member-form.html");

                try (PrintWriter out = response.getWriter()) {
                    htmlAddBook(out, request); //TODO...
                    //tml);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error: " + e);
                }
            }
            case "/update" -> {
                String bookIdToEdit = request.getParameter("bookId");
                try {
                    int bookId = Integer.parseInt(bookIdToEdit);
                    Books book = booksDAO.getBookById(bookId); //TODO...
                    if (book != null) {

                        PrintWriter out = response.getWriter();
                        htmlUpdate(out, request, book);

                    } else {
                        response.getWriter().println("Error: Book with ID " + bookId + " not found for editing.");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            case null, default -> {
                try (PrintWriter out = response.getWriter()) {
                    out.println("<h1>Not Found</h1><p>The requested resource was not found.</p>");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String pathInfo = request.getPathInfo();

        if ("/add".equals(pathInfo)) {
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            int pageCount = Integer.parseInt(request.getParameter("pageCount"));

            String publisher = request.getParameter("publisher");
            String datePublished = request.getParameter("datePublished");
            String libraryCopies = request.getParameter("libraryCopies");
            String availableCopies = request.getParameter("availableCopies");


            Books newBook = new Books();
            newBook.setTitle(title);
            newBook.setAuthor(author);
            newBook.setPage_count(pageCount);
            newBook.setPublisher(publisher);
            newBook.setDate_published(Date.valueOf(datePublished));
            newBook.setLibrary_copies(Integer.parseInt(libraryCopies));
            newBook.setAvailable_copies(Integer.parseInt(availableCopies));
            ;
            try {
                booksDAO.insertBooks(newBook);
                response.sendRedirect(request.getContextPath() + "/books");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else if (("/delete".equals(pathInfo))) {
            String bookRemove = request.getParameter("bookId");
            try {
                int bookId = Integer.parseInt(bookRemove);
                boolean deleted = booksDAO.deleteBooks(bookId);
                if (deleted) {
                    response.sendRedirect(request.getContextPath() + "/books");
                } else {
                    // for debugging right now
                    System.out.println("Book not found");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // relatively similar to //add
        } else if (("/update".equals(pathInfo))) {
            String updateId = request.getParameter("bookId");

            String title = request.getParameter("title");
            String author = request.getParameter("author");
            int pageCount = Integer.parseInt(request.getParameter("pageCount"));
            String publisher = request.getParameter("publisher");
            String datePublished = request.getParameter("datePublished");
            String libraryCopies = request.getParameter("libraryCopies");
            String availableCopies = request.getParameter("availableCopies");

            try {
                int bookId = Integer.parseInt(updateId);
                Books updatedBook = new Books();

                updatedBook.setTitle(title);
                updatedBook.setAuthor(author);
                updatedBook.setPage_count(pageCount);
                updatedBook.setPublisher(publisher);
                updatedBook.setDate_published(Date.valueOf(datePublished));
                updatedBook.setLibrary_copies(Integer.parseInt(libraryCopies));
                updatedBook.setAvailable_copies(Integer.parseInt(availableCopies));
                updatedBook.setBook_id(bookId);


                boolean updated = booksDAO.updateBooks(updatedBook);
                if (updated) {
                    response.sendRedirect(request.getContextPath() + "/books");
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

    public void htmlUpdate(PrintWriter out, HttpServletRequest request, Books book) {
        // both ID and Name have same value -> getElementById
        // Can implement this as a script later for JS
        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_form_submit
        // This example came in handy since it showed with prefilled attributes
        // needed since we are reusing add methods for updating
        // find out about labels being printed
        // TODO... INPUT VALIDATION
        // css make ->> https://www.w3schools.com/css/tryit.asp?filename=trycss_forms

        //language=html
        out.print(STR."""
        <!DOCTYPE html>
        <html lang="en">
        <body>
        <head>
            <title>Update book | Library Management</title>
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
               \s
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
               \s
                input[type=submit]:hover {
                  background-color: #1b727f;
                }
               \s
                div {
                  border-radius: 5px;
                  background-color: #f2f2f2;
                  padding: 20px;
                }

            </style>
        </head>
        <body>

        <h2>Update Book</h2>

        <form action='\{request.getContextPath()}/books/update' method='post'>
            <input type='hidden' id='bookId' name='bookId' value='\{book.getBook_id()}'>

            <div>
                <h3>Title</h3>

                <input type='text' id='title' name='title' value='\{book.getTitle()}'>
            </div>

            <div>
                <h3>Author</h3>
                <input type='text' id='author' name='author' value='\{book.getAuthor()}'>
            </div>

            <div>
                <h3>Page Count</h3>
                <input type='text' id='pageCount' name='pageCount' value='\{book.getPage_count()}'>
            </div>
            <div>
                <h3>Publisher</h3>
                <input type='text' id='publisher' name='publisher' value='\{book.getPublisher()}'>
            </div>

            <div>
                <h3>Date Published</h3>
                <input type='text' id='datePublished' name='datePublished' value='\{book.getDate_published()}'>
            </div>

            <div>
                <h3>Library Copies</h3>
                <input type='text' id='libraryCopies' name='libraryCopies' value='\{book.getLibrary_copies()}'>
            </div>

            <div>
                <h3>Available Copies</h3>
                <input type='text' id='availableCopies' name='availableCopies' value='\{book.getAvailable_copies()}'>
            </div>

            <!-- add cancel to revert back to pages members -->
            <input type='submit' value='Update'>

        </form>
        </body>
        </html>""");
    }

    private void htmlMainTable(PrintWriter out) {

        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_table_border
        // Use this to make tables recall header vs cell data
        // See example servet code 9.18
        // Make main bar to navigate to info

        //language=html
        out.print(STR."""
            <html><body>
            <head>
            <title>Books | Library Management</title>
            <style>
            table {
              border: 1px;
              margin-left: auto;\s
              margin-right: auto;
              width: 90%;
              max-width: 1000px;
            }
            
            th, td {
              text-align: center;
              padding: 8px;
              border-bottom: 1px solid #ddd;
            }
            
            tr:nth-child(even){background-color: #f2f2f2}
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
              font-size: 18px;\s
              line-height: 25px;
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
            
            
            </style>
            </head>
            <body>
            
            <div class="header">
                <div class="header-right">
                <a class="active" href="/">Home</a>
                <a href="/books/add">Add Book</a>
            </div>
            </div>
            
            <!-- TODO... Style and implent script to search by title? -->
            <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for names.." title="Type in a name">
            <table><tr><th>Book ID</th><th>Title</th><th>Author</th><th>Page Count</th><th>Publisher</th><th>Date Published</th><th>Library Copies</th><th>Available Copies</th><th>EDIT</th><th>DELETE</th>
            """);
    }

    private void htmlListMembers(PrintWriter out, List<Books> allBooks) {
        if (allBooks == null) {
            System.out.println("<h1>No BOOKS found.</h1>");
            return;
        }

        for (Books book : allBooks) {
            //language=html
            out.print(STR."""
                <tr>
                    <td>\{book.getBook_id()}</td>
                    <td>\{book.getTitle()}</td>
                    <td>\{book.getAuthor()}</td>
                    <td>\{book.getPage_count()}</td>
                    <td>\{book.getPublisher()}</td>
                    <td>\{book.getDate_published()}</td>
                    <td>\{book.getLibrary_copies()}</td>
                    <td>\{book.getAvailable_copies()}</td>
                    <td>
                        <form action=/books/update method=get>
                            <input type=hidden name=bookId value=\{book.getBook_id()}>
                            <button type=submit>Update</button>
                        </form>
                    </td>
                    <td>
                        <form action=books/delete method=post>
                            <input type=hidden name=bookId value=\{book.getBook_id()}>
                            <button type=submit>Delete</button>
                        </form>
                    </td>
                    <!-- TODO generate remaining columns (view member fines, borrowed books, etc) -->>
                </tr>""");
        }


    }

    public void htmlAddBook(PrintWriter out, HttpServletRequest request) {
        // both ID and Name have same value -> getElementById
        // Can implement this as a script later for JS
        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_form_submit
        // This example came in handy since it showed with prefilled attributes
        // needed since we are reusing add methods for updating
        // find out about labels being printed
        // TODO... INPUT VALIDATION
        // css make ->> https://www.w3schools.com/css/tryit.asp?filename=trycss_forms

        //language=html
        out.print(STR."""
        <!DOCTYPE html>
        <html>
        <head>
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
               \s
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
               \s
                input[type=submit]:hover {
                  background-color: #1b727f;
                }
               \s
                div {
                  border-radius: 5px;
                  background-color: #f2f2f2;
                  padding: 20px;
                }

            </style>
        </head>
        <body>

        <h2>Add Book</h2>

        <form action='\{request.getContextPath()}/books/add' method='post'>
            <input type='hidden' id='bookId' name='bookId' value='" + "'>

            <div>
                <h3>Title</h3>

                <input type='text' id='title' name='title' value='" + "'>
            </div>

            <div>
                <h3>Author</h3>
                <input type='text' id='author' name='author' value='" + "'>
            </div>

            <div>
                <h3>Page Count</h3>
                <input type='text' id='pageCount' name='pageCount' value='" + "'>
            </div>
            <div>
                <h3>Publisher</h3>
                <input type='text' id='publisher' name='publisher' value='" + "'>
            </div>

            <div>
                <h3>Date Published</h3>
                <input type='text' id='datePublished' name='datePublished' value='" + "'>
            </div>

            <div>
                <h3>Library Copies</h3>
                <input type='text' id='libraryCopies' name='libraryCopies' value='" + "'>
            </div>

            <div>
                <h3>Available Copies</h3>
                <input type='text' id='availableCopies' name='availableCopies' value='" + "'>
            </div>

            <!-- add cancel to revert back to pages members -->
            <input type='submit' value='Add BOOK'>

        </form>
        </body>
        </html>""");
    }

}
