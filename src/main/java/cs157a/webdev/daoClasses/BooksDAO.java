package cs157a.webdev.daoClasses;



import cs157a.webdev.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDAO {
    private static final String SELECT_BY_ID = "SELECT book_id, title, author, page_count, publisher, date_published, library_copies, available_copies FROM books WHERE book_id = ?;";
    private static final String SORT_BY_ID = "SELECT book_id, title, author, page_count, publisher, date_published, library_copies, available_copies FROM books " + "ORDER BY book_id;";
    private static final String SELECT_ALL_BOOKS = "SELECT * FROM Books";
    private static final String DELETE_BOOKS_SQL = "DELETE FROM Books where book_id = ?;";
    // TODO... made id SERIAL choose whether to handle it or not, how to handle it
    private static final String UPDATE_BOOKS_SQL = "UPDATE Books SET title= ?, author = ?, page_count = ?, publisher = ?, date_published = ?, library_copies = ?, available_copies = ? where book_id = ?;";
    private static final String INSERT_BOOKS_SQL = "INSERT INTO Books (title, author, page_count, publisher, date_published, library_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?);";



    // establish connection
    public static Connection getConnection() throws SQLException {
        Connection con = null;

        try{
            Class.forName("org.postgresql.Driver");
            con= DriverManager.getConnection(DbCreds.url + DbCreds.dbName, DbCreds.dbUser, DbCreds.dbPassword);
            if(con!=null)
            {
                System.out.println("Connected to PostgreSQL database");

            }else{
                System.out.println("Failed to connect to PostgreSQL database");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return con;
    }


    public List<Books> selectAllBooks() throws SQLException {

        List <Books> book = new ArrayList<>();
        try (Connection connection = getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BOOKS);) {
            ResultSet rs = preparedStatement.executeQuery();
            // (book_id, title, author, page_count, publisher, date_published, library_copies, available)
            while (rs.next()) {
                int book_id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int page_count = rs.getInt("page_count");
                String publisher = rs.getString("publisher");
                Date date_published = rs.getDate("date_published");
                int library_copies = rs.getInt("library_copies");
                int available_copies = rs.getInt("available_copies");


                book.add(new Books(book_id, title, author, page_count, publisher, date_published, library_copies, available_copies));
                //  (int fine_id, int br_id, float fine_total, String fine_status)
            }
        }

        return book;
    }


    public boolean deleteBooks(int id) throws SQLException {
        boolean result;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_BOOKS_SQL);) {
            statement.setInt(1, id);
            boolean check_success = statement.executeUpdate() > 0;
            if (check_success) {
                System.out.println("Books deleted with id: " + id);
                result = true;
            } else
            {
                System.out.println("Books deletion failed");
                result = false;
            }

        }

        return result;
    }



    public void insertBooks(Books book) throws SQLException
    {
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
            preparedStatement.executeUpdate();
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

    public List<Books> sortBooksAsc() throws SQLException {
        List<Books> sortedBooks = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SORT_BY_ID);
             ResultSet rs = statement.executeQuery()) {
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
        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return book;
    }

}










/*
// Testing methods
    public static void main(String[] args) throws SQLException {
        BooksDAO booksDAO = new BooksDAO();
        Books book = new Books();

        book.setBook_id(1);
        book.setTitle("MYBOOKTITLE");
        book.setAuthor("MYAUTHOR");
        book.setPage_count(100);
        book.setPublisher("MYPUBLISHER");

        LocalDate signupLocalDate = LocalDate.now();
        Date signupDate = Date.valueOf(signupLocalDate);
        book.setDate_published(signupDate);

        book.setLibrary_copies(4);
        book.setAvailable_copies(4);

        //(book_id, title, author, page_count, publisher, date_published, library_copies, available)
        booksDAO.insertBooks(book);

    }
*/



