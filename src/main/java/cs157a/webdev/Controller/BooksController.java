package cs157a.webdev.Controller;

import cs157a.webdev.daoClasses.BooksDAO;
import cs157a.webdev.model.Books;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

//We need to  create a class that inherits from HttpServlet
// while doing so we overrides methods doGet, doPost

public class BooksController extends HttpServlet {

    private final BooksDAO booksDAO = new BooksDAO();

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
                List <Books> allBooks = booksDAO.sortBooksAsc(); //TODO...
                htmlListMembers(out, allBooks);

            } catch (Exception e) // check if valid
            {
                System.out.println(e);
            }

        } else if ("/add".equals(pathInfo)) {
            //String html = readFile("view/add-member-form.html");

            try (PrintWriter out = response.getWriter()) {
                htmlAddBook(out, request); //TODO...
                //out.println(html);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e);
            }
        } else if ("/update".equals(pathInfo)) {
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

        out.println("<h2>Update Book</h2>\n");

        out.println("<form action='" + request.getContextPath() + "/books/update' method='post'>");
        //out.println("label for=\"memberId\">First name:</label><br>");
        out.println("<input type='hidden' id='bookId' name='bookId' value='" + book.getBook_id() + "'>");

        out.println("<div>");
        out.println("<h3>Title</h3>");

        //out.println("label for=\"firstName\">First name:</label><br>");
        out.println("<input type='text' id='title' name='title' value='" + book.getTitle() + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Author</h3>");
        out.println("<input type='text' id='author' name='author' value='" + book.getAuthor() + "'>");
        out.println("</div>");

        //out.println("label for=\"email\">email:</label><br>");
        out.println("<div>");
        out.println("<h3>Page Count</h3>");
        out.println("<input type='text' id='pageCount' name='pageCount' value='" + book.getPage_count() + "'>");
        out.println("</div>");
/////
        out.println("<div>");
        out.println("<h3>Publisher</h3>");
        out.println("<input type='text' id='publisher' name='publisher' value='" + book.getPublisher() + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Date Published</h3>");
        out.println("<input type='text' id='datePublished' name='datePublished' value='" + book.getDate_published() + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Library Copies</h3>");
        out.println("<input type='text' id='libraryCopies' name='libraryCopies' value='" + book.getLibrary_copies() + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Available Copies</h3>");
        out.println("<input type='text' id='availableCopies' name='availableCopies' value='" + book.getAvailable_copies() + "'>");
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
        out.println("    <a href=\"/books/add\">Add Book</a>\n");
        out.println("</div>\n");
        out.println("</div>");


        // TODO... Style and implent script to search by title?
        out.println("<input type=\"text\" id=\"myInput\" onkeyup=\"myFunction()\" placeholder=\"Search for names..\" title=\"Type in a name\">");


        out.println("<table border=1><tr>" +
                "<th>Book ID</th>" +
                "<th>Title</th>" +
                "<th>Author</th>" +
                "<th>Page Count</th>" +
                "<th>Publisher</th>" +
                "<th>Date Published</th>" +
                "<th>Library Copies</th>" +
                "<th>Available Copies</th>" +
                "<th>EDIT</th>" +
                "<th>DELETE</th>");

    }

    private void htmlListMembers(PrintWriter out, List<Books> allBooks) {

        if (allBooks != null) {
            for (Books book: allBooks) {
                out.println("<tr>");

                out.println("<td>" + book.getBook_id() + "</td>");
                out.println("<td>" + book.getTitle() + "</td>");
                out.println("<td>" + book.getAuthor() + "</td>");
                out.println("<td>" + book.getPage_count() + "</td>");
                out.println("<td>" + book.getPublisher() + "</td>");
                out.println("<td>" + book.getDate_published() + "</td>");
                out.println("<td>" + book.getLibrary_copies() + "</td>");
                out.println("<td>" + book.getAvailable_copies() + "</td>");


                // TODO... check why only works for http://localhost:9999/members vs /members/
                out.println("<td>");
                // /members/update fixed but still check
                out.println("<form action=/books/update method=get>");
                out.println("<input type=hidden name=bookId value=" + book.getBook_id() + ">");
                out.println("<button type=submit>Update</button>");
                out.println("</form>");
                out.println("</td>");

                out.println("<td>");
                out.println("<form action=books/delete method=post>");
                out.println("<input type=hidden name=bookId value=" + book.getBook_id() + ">");
                out.println("<button type=submit>Delete</button>");

                out.println("</form>");
                out.println("</td>");

                // generate remaining columns (view member fines, borrowed books, etc)

                out.println("  </tr>");
                //out.close();

            }

        } else {
            System.out.println("<h1>No BOOKS found.</h1>");
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

        out.println("<h2>Add Book</h2>\n");

        out.println("<form action='" + request.getContextPath() + "/books/add' method='post'>");
        //out.println("label for=\"memberId\">First name:</label><br>");
        out.println("<input type='hidden' id='bookId' name='bookId' value='" + "'>");

        out.println("<div>");
        out.println("<h3>Title</h3>");

        //out.println("label for=\"firstName\">First name:</label><br>");
        out.println("<input type='text' id='title' name='title' value='" + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Author</h3>");
        out.println("<input type='text' id='author' name='author' value='" + "'>");
        out.println("</div>");

        //out.println("label for=\"email\">email:</label><br>");
        out.println("<div>");
        out.println("<h3>Page Count</h3>");
        out.println("<input type='text' id='pageCount' name='pageCount' value='" + "'>");
        out.println("</div>");
        ///
        out.println("<div>");
        out.println("<h3>Publisher</h3>");
        out.println("<input type='text' id='publisher' name='publisher' value='" + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Date Published</h3>");
        out.println("<input type='text' id='datePublished' name='datePublished' value='" + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Library Copies</h3>");
        out.println("<input type='text' id='libraryCopies' name='libraryCopies' value='" + "'>");
        out.println("</div>");

        out.println("<div>");
        out.println("<h3>Available Copies</h3>");
        out.println("<input type='text' id='availableCopies' name='availableCopies' value='" + "'>");
        out.println("</div>");

        // add cancel to revert back to pages members
        out.println("<input type='submit' value='Add BOOK'>");

        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

    }

}
