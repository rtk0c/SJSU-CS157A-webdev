package cs157a.webdev;

import cs157a.webdev.model.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.*;

import java.sql.*;

/**
 * Handle both inserting new books, and updating existing books.
 */
public class HttpBook extends BaseHttpHandler {
    @Override
    protected String handleGet(Request req) throws Exception {
        // both ID and Name have same value -> getElementById
        // Can implement this as a script later for JS
        // https://www.w3schools.com/html/tryit.asp?filename=tryhtml_form_submit
        // This example came in handy since it showed with prefilled attributes
        // needed since we are reusing add methods for updating
        // find out about labels being printed
        // css make ->> https://www.w3schools.com/css/tryit.asp?filename=trycss_forms

        MultiMap<String> params = UrlEncoded.decodeQuery(req.getHttpURI().getQuery());

        Books book;
        String bookIdField;

        var bookId = params.getString("bookId");
        if (bookId == null) {
            // Insert
            book = new Books(); // load dummy values
            bookIdField = "";
        } else {
            // Update
            book = Db.books.getBookById(Integer.parseInt(bookId));
            bookIdField = STR."<input type='hidden' id='bookId' name='bookId' value='\{book.getBook_id()}'>";
        }

        //language=html
        return STR."""
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
        </style>
        </head>
        <body>
        <h2>Update Book</h2>
        <form method='post'>
            \{bookIdField}
            <label>
                Title
                <input type='text' name='title' value='\{book.getTitle()}'>
            </label>
            <label>
                Author
                <input type='text' name='author' value='\{book.getAuthor()}'>
            </label>
            <label>
                Page Count
                <input type='text' name='pageCount' value='\{book.getPage_count()}'>
            </label>
            <label>
                Publisher
                <input type='text' name='publisher' value='\{book.getPublisher()}'>
            </label>
            <label>
                Date Published
                <input type='text' name='datePublished' value='\{book.getDate_published()}'>
            </label>
            <label>
                Library Copies
                <input type='text' name='libraryCopies' value='\{book.getLibrary_copies()}'>
            </label>
            <label>
                Available Copies
                <input type='text' name='availableCopies' value='\{book.getAvailable_copies()}'>
            </label>
            <!-- add cancel to revert back to pages members -->
            <input type='submit' value='Submit'>
        </form>
        </body>
        </html>""";
    }

    @Override
    protected String handlePost(Request req) throws Exception {
        MultiMap<String> params = UrlEncoded.decodeQuery(req.getHttpURI().getQuery());

        String title = params.getValue("title");
        String author = params.getValue("author");
        int pageCount = Integer.parseInt(params.getValue("pageCount"));
        String publisher = params.getValue("publisher");
        String datePublished = params.getValue("datePublished");
        String libraryCopies = params.getValue("libraryCopies");
        String availableCopies = params.getValue("availableCopies");

        Books b = new Books();
        b.setTitle(title);
        b.setAuthor(author);
        b.setPage_count(pageCount);
        b.setPublisher(publisher);
        b.setDate_published(Date.valueOf(datePublished));
        b.setLibrary_copies(Integer.parseInt(libraryCopies));
        b.setAvailable_copies(Integer.parseInt(availableCopies));

        var bookId = params.getString("bookId");
        if (bookId == null) {
            // Insert
            Db.books.insertBooks(b);
        } else {
            // Update
            b.setBook_id(Integer.parseInt(bookId));
            Db.books.updateBooks(b);
        }

        responseType = HTTP_REDIRECT;
        return "/books";
    }

    @Override
    protected String handleDelete(Request req) throws Exception {
        MultiMap<String> params = UrlEncoded.decodeQuery(req.getHttpURI().getQuery());

        String bookRemove = params.getValue("bookId");
        int bookId = Integer.parseInt(bookRemove);
        boolean deleted = Db.books.deleteBooks(bookId);
        if (deleted) {
            responseType = HTTP_REDIRECT;
            return "/books";
        }

        return "Book not found";

        // relatively similar to //add
    }
}
