package cs157a.webdev.daoClasses;

import cs157a.webdev.*;
import cs157a.webdev.model.*;

import java.sql.*;
import java.text.*;
import java.time.temporal.*;
import java.util.ArrayList;
import java.util.List;

public class Borrow_ReturnsDAO {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String INSERT_BR_SQL = "INSERT INTO Borrow_Returns (member_id, book_id, borrow_date, return_date, due_date) VALUES (?, ?, ?, ?, ?);";
    private static final String JOIN_TABLES = "SELECT br.br_id, m.member_id, b.book_id, br.borrow_date, br.return_date, br.due_date FROM Borrow_Returns br INNER JOIN Members m ON br.member_id = m.member_id INNER JOIN Books b ON br.book_id = b.book_id WHERE br.member_id = ? ORDER BY br.br_id;";
    private static final String SELECT_BY_MEMBER_ID = "SELECT br_id, member_id, book_id, borrow_date, due_date, return_date  FROM borrow_returns WHERE member_id = ?";
 // private static final String UPDATE_BORROW_RETURN_STATUS = "UPDATE borrow_returns SET borrowed_book_status = ? WHERE br_id = ?";


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
                br.setMember_id(rs.getInt("member_id"));
                br.setBook_id(rs.getInt("book_id"));
                br.setBorrow_date(rs.getDate("borrow_date"));
                br.setReturn_date(rs.getDate("return_date"));
                br.setDue_date(rs.getDate("due_date"));

                sortedBR.add(br);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sortedBR;
    }


    private int calculateFine(Date dueDate, Date returnDate) {
        var timeDifference = returnDate.getTime() - dueDate.getTime();
        var daysLate = timeDifference / (1000 * 60 * 60 * 24.0);
        long roundedDaysLate = Math.round(daysLate);
        //System.out.println(timeDifference+"t");
        //System.out.println(daysLate+"d");
        //System.out.println(roundedDaysLate + "L");
        return (int) (roundedDaysLate * 2);
    }
    public void checkFines(int memberId) throws SQLException {
        ResultSet rs = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_MEMBER_ID)) {
            statement.setInt(1, memberId);
            rs = statement.executeQuery();

            while (rs.next()) {
                Date returnDate = rs.getDate("return_date");
                Date dueDate = rs.getDate("due_date");
                int brId = rs.getInt("br_id");

                if (returnDate != null && returnDate.after(dueDate)) {
                    // Check if a fine already exists for our br_id
                    if (!Db.fines.doesFineExistForBrId(brId)) {
                        Fines newFine = new Fines();
                        newFine.setBr_id(brId);
                        newFine.setFine_total(calculateFine(dueDate, returnDate));
                        newFine.setFine_status(true); // we make fine true as default since ret. late
                        newFine.setMember_id(memberId);
                        Db.fines.insertFine(newFine);
                        System.out.println("FINE inserted for BR ID: " + brId + ", Member ID: " + memberId);
                    } else {
                        System.out.println("Fine already exists for BR ID: " + brId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

