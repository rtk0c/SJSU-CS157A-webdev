package cs157a.webdev.daoClasses;


import cs157a.webdev.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDAO {
    private static final String SELECT_BY_ID = "SELECT book_id, title, author, page_count, publisher, date_published, library_copies, available_copies FROM books WHERE book_id = ?;";
    private static final String SORT_BY_ID = """
        SELECT book_id, title, author, page_count, publisher, date_published, library_copies, available_copies
        FROM books
        ORDER BY book_id;
        """;
    private static final String SORT_BY_ID_FILTER = """
        SELECT book_id, title, author, page_count, publisher, date_published, library_copies, available_copies
        FROM books
        WHERE title ILIKE ?
        ORDER BY book_id;
        """;
    private static final String DELETE_BOOKS_SQL = "DELETE FROM Books where book_id = ?;";
    // TODO... made id SERIAL choose whether to handle it or not, how to handle it
    private static final String UPDATE_BOOKS_SQL = "UPDATE Books SET title= ?, author = ?, page_count = ?, publisher = ?, date_published = ?, library_copies = ?, available_copies = ? where book_id = ?;";
    private static final String INSERT_BOOKS_SQL = "INSERT INTO Books (title, author, page_count, publisher, date_published, library_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?);";


    // establish connection
    public static Connection getConnection() throws SQLException {
        Connection con = null;

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(DbCreds.url + DbCreds.dbName, DbCreds.dbUser, DbCreds.dbPassword);
            if (con != null) {
                System.out.println("Connected to PostgreSQL database");

            } else {
                System.out.println("Failed to connect to PostgreSQL database");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }


    public boolean deleteBooks(int id) throws SQLException {
        boolean result;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_BOOKS_SQL);) {
            statement.setInt(1, id);
            boolean check_success = statement.executeUpdate() > 0;
            if (check_success) {
                System.out.println("Books deleted with id: " + id);
                result = true;
            } else {
                System.out.println("Books deletion failed");
                result = false;
            }

        }

        return result;
    }


    public void insertBooks(Books book) throws SQLException {
//(book_id, title, author, page_count, publisher, date_published, library_copies, available)
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOKS_SQL);) {

            //preparedStatement.setInt(1, book.getBook_id());
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getPage_count());
            preparedStatement.setString(4, book.getPublisher());
            preparedStatement.setDate(5, book.getDate_published());
            preparedStatement.setInt(6, book.getLibrary_copies());
            preparedStatement.setInt(7, book.getAvailable_copies());

            preparedStatement.executeUpdate();
            System.out.println("Books Inserted Successfully...");
        } catch (SQLException e) {
            System.out.println("Error Insert Books" + e);
        }
    }


    public boolean updateBooks(Books book) throws SQLException {
        boolean check_success;
//(book_id, title, author, page_count, publisher, date_published, library_copies, available)
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_BOOKS_SQL);) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getPage_count());
            statement.setString(4, book.getPublisher());
            statement.setDate(5, book.getDate_published());
            statement.setInt(6, book.getLibrary_copies());
            statement.setInt(7, book.getAvailable_copies());
            statement.setInt(8, book.getBook_id());

            check_success = statement.executeUpdate() > 0;

        }
        return check_success;
    }


    // TODO... FINISH CHECK METHOD
    public Boolean updateBooksAvailable(Books book) throws SQLException {
        boolean check_success = true;
        //(book_id, title, author, page_count, publisher, date_published, library_copies, available)
        // can change to just upgrade books available
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_BOOKS_SQL);) {

            if (book.getAvailable_copies() <= 0) {
                check_success = false;
                return check_success;
            }

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getPage_count());
            statement.setString(4, book.getPublisher());
            statement.setDate(5, book.getDate_published());
            statement.setInt(6, book.getLibrary_copies());
            statement.setInt(7, (book.getAvailable_copies() - 1));
            //System.out.println(book.getAvailable_copies()-1);
            statement.setInt(8, book.getBook_id());

            statement.executeUpdate();

        }
        return check_success;
    }


    public List<Books> sortBooksAsc(String filter) throws SQLException {
        List<Books> sortedBooks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(filter == null ? SORT_BY_ID : SORT_BY_ID_FILTER)) {
            if (filter != null) {
                statement.setString(1, filter);
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                // similar to og method should check why this should be default...
                Books book = new Books();
                book.setBook_id(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPage_count(rs.getInt("page_count"));
                book.setPublisher(rs.getString("publisher"));
                book.setDate_published(rs.getDate("date_published"));
                book.setLibrary_copies(rs.getInt("library_copies"));

                book.setAvailable_copies(rs.getInt("available_copies"));

                sortedBooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sortedBooks;
    }

    public Books getBookById(int id) throws SQLException {
        ResultSet resultSet = null;
        Books book = null;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);) {
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                book = new Books();
                book.setBook_id(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPage_count(resultSet.getInt("page_count"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setDate_published(resultSet.getDate("date_published"));
                book.setLibrary_copies(resultSet.getInt("library_copies"));
                book.setAvailable_copies(resultSet.getInt("available_copies"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return book;
    }

}

