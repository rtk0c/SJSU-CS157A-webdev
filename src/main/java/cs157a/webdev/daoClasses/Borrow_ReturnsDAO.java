package cs157a.webdev.daoClasses;

import cs157a.webdev.model.*;

import java.sql.*;
import java.text.*;
import java.util.ArrayList;
import java.util.List;

public class Borrow_ReturnsDAO {

    private static final String INSERT_BR_SQL = "INSERT INTO Borrow_Returns (member_id, book_id, borrow_date, return_date, due_date) VALUES (?, ?, ?, ?, ?);";
    private static final String JOIN_TABLES = "SELECT br.br_id, m.member_id, b.book_id, br.borrow_date, br.return_date, br.due_date FROM Borrow_Returns br INNER JOIN Members m ON br.member_id = m.member_id INNER JOIN Books b ON br.book_id = b.book_id WHERE br.member_id = ? ORDER BY br.br_id;";
    private static final String SELECT_BY_MEMBER_ID = "SELECT br_id, member_id, book_id, borrow_date, due_date, return_date  FROM borrow_returns WHERE member_id = ?";
    private static final String UPDATE_BORROW_RETURN_STATUS = "UPDATE borrow_returns SET borrowed_book_status = ? WHERE br_id = ?";


    // In Borrow_ReturnsDAO.java

    public boolean updateBorrowReturnStatus(int brId, String newStatus) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_BORROW_RETURN_STATUS)) {
            statement.setString(1, newStatus);
            statement.setInt(2, brId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Borrow_Returns> getBorrowReturnsByMemberId(int memberId) throws SQLException {
        List<Borrow_Returns> borrowReturns = new ArrayList<>();
        ResultSet rs = null;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_BY_MEMBER_ID)) {
            statement.setInt(1, memberId);
            rs = statement.executeQuery();

            while (rs.next()) {
                Borrow_Returns br = new Borrow_Returns();
                //br.setBr_id(rs.getInt("br_id"));
                br.setMember_id(rs.getInt("book_id"));
                br.setMember_id(rs.getInt("member_id"));
                br.setBorrow_date(rs.getDate("borrow_date"));
                br.setDue_date(rs.getDate("due_date"));
                br.setReturn_date(rs.getDate("return_date"));
                //br.setBorrowed_book_status(rs.getString("borrowed_book_status"));
                borrowReturns.add(br);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return borrowReturns;
    }

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


    public void insertBR(Borrow_Returns br) throws SQLException {
        System.out.println(INSERT_BR_SQL);
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BR_SQL)) {
            //preparedStatement.setInt(1, mem.getMember_id()); // incremented when user fills out new form with if SERIAL?
            preparedStatement.setInt(1, br.getMember_id());
            preparedStatement.setInt(2, br.getBook_id());
            preparedStatement.setDate(3, br.getBorrow_date());
            preparedStatement.setDate(4, br.getReturn_date());
            preparedStatement.setDate(5, br.getDue_date());

            System.out.println("Manual B/R Inserted Successfully...");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Insert B/R" + e);
        }
    }


    public List<Borrow_Returns> sortBRAsc(int memId) throws SQLException {
        List<Borrow_Returns> sortedBR = new ArrayList<>();
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(JOIN_TABLES)) {

            statement.setInt(1, memId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Borrow_Returns br = new Borrow_Returns();
                //br.setBr_id(rs.getInt("br_id"));
                br.setMember_id(rs.getInt("member_id"));
                br.setBook_id(rs.getInt("book_id"));
                //br.setTitle(rs.getString("title"));
                br.setBorrow_date(rs.getDate("borrow_date"));
                br.setReturn_date(rs.getDate("return_date"));
                br.setDue_date(rs.getDate("due_date"));
                //br.setBorrowed_book_status(rs.getString("borrowed_book_status"));


                sortedBR.add(br);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sortedBR;
    }


}

