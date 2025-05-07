package cs157a.webdev;

import cs157a.webdev.model.*;
import org.eclipse.jetty.server.*;

import java.util.*;

public class HttpBookList extends BaseHttpHandler {
    @Override
    protected String handleGet(Request req) throws Exception {
        var params = getHtmlFormParams(req);

        String filter = params.getValue("filter");
        String filterBannerText;
        if (filter != null) {
            if (!filter.startsWith("%"))
                filter = "%" + filter;
            if (!filter.endsWith("%"))
                filter = filter + "%";
            filterBannerText = STR."<p>Filter: <span>\{filter}</span></p>";
        } else {
            filterBannerText = "";
        }
        List<Books> allBooks = Db.books.sortBooksAsc(filter);

        //language=html
        return STR."""
            <html><body>
            <head>
            <title>Books | Library Management</title>
            <style>
            table {
              border: 1px;
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
              font-size: 18px;
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
            <script>
            function deleteThisBook(book) {
              fetch(`/book?bookId=${book.dataset.bookid}`, {
                method: 'DELETE',
              })
              alert('Book deleted. Refresh page to see the updated book list.')
            }
            </script>
            </head>
            <body>

            <div class="header">
                <div class="header-right">
                <a class="active" href="/">Home</a>
                <a href="/book">Add Book</a>
            </div>
            </div>

            <form method="get">
                <input type="text" placeholder="Search for title.." name="filter">
                <input type="submit" value="Search">
            </form>
            \{filterBannerText}
            <table>
            <tr>
                <th>Book ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Page Count</th>
                <th>Publisher</th>
                <th>Date Published</th>
                <th>Library Copies</th>
                <th>Available Copies</th>
                <th>EDIT</th>
                <th>DELETE</th>
                <th>Checkout</th>
            </tr>
            \{htmlListBooks(allBooks)}
            </table>
            """;
    }

    private String htmlListBooks(List<Books> allBooks) {
        if (allBooks == null) {
            return "<h1>No BOOKS found.</h1>";
        }

        var sb = new StringBuilder();
        for (Books book : allBooks) {
            //language=html
            sb.append(STR."""
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
                        <form action='/book' method='get'>
                            <input type=hidden name=bookId value=\{book.getBook_id()}>
                            <button type=submit>Update</button>
                        </form>
                    </td>
                    <td>
                        <button onclick='deleteThisBook(this)' data-bookid='\{book.getBook_id()}'>Delete</button>
                    </td>
                    <td>
                         <form action='/borrows/checkout' method='get'>
                            <input type=hidden name=bookId value='\{book.getBook_id()}' >
                            <button type=submit>Check Out</button>
                        </form>
                    </td>
                </tr>""");
        }
        return sb.toString();
    }
}
