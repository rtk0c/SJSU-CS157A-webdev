package cs157a.webdev.daoClasses;

import cs157a.webdev.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinesDAO {

    // TODO... made id SERIAL choose whether to handle it or not, how to handle it
    private static final String UPDATE_FINES_SQL = "UPDATE Fines SET fine_total = ?, fine_status = ? WHERE br_id = ?;";
    private static final String SELECT_BY_ID = """
    SELECT Members.member_id, fine_total, fine_status, Borrow_Returns.br_id FROM Fines
        JOIN Borrow_Returns ON Fines.br_id = Borrow_Returns.br_id
        JOIN Members ON Borrow_Returns.member_id = Members.member_id
    WHERE Members.member_id = ?
    ORDER BY Borrow_Returns.br_id;
        """;
    private static final String SELECT_FINE_BY_BR_ID_FOR_UPDATE = "SELECT * FROM Fines WHERE br_id = ?";
    private static final String SELECT_FINE_BY_BR_ID = "SELECT COUNT(*) FROM fines WHERE br_id = ?";
    private static final String INSERT_FINE = "INSERT INTO fines (br_id, fine_total, fine_status) VALUES (?, ?, ?)";

    public boolean doesFineExistForBrId(int brId) throws SQLException {
        ResultSet rs = null;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_FINE_BY_BR_ID)) {
            statement.setInt(1, brId);
            rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertFine(Fines fine) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_FINE)) {
            statement.setInt(1, fine.getBr_id());
            statement.setInt(2, fine.getFine_total());
            statement.setBoolean(3, fine.getFine_status());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public Fines getFineById(int brId) throws SQLException {
        ResultSet resultSet = null;
        Fines fine = null;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_FINE_BY_BR_ID_FOR_UPDATE);) {
            statement.setInt(1, brId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                fine = new Fines();
                fine.setBr_id(resultSet.getInt("br_id"));
                fine.setFine_total(resultSet.getInt("fine_total"));
                fine.setFine_status(resultSet.getBoolean("fine_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return fine;
    }

    public List<Fines> sortFinesAsc(int memberId) throws SQLException {
        List<Fines> sortedFines = new ArrayList<>();
        ResultSet rs = null;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);) {
            statement.setInt(1, memberId);
            rs = statement.executeQuery();


            while (rs.next()) {
                // similar to og method should check why this should be default...
                Fines fine = new Fines();
                //fine.setFine_id(rs.getInt("fine_id"));
                fine.setBr_id(rs.getInt("br_id"));
                fine.setFine_total(rs.getInt("fine_total"));
                fine.setFine_status(rs.getBoolean("fine_status"));
                sortedFines.add(fine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sortedFines;
    }


    public boolean updateFine(Fines fin) throws SQLException {
        boolean check_success;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_FINES_SQL);) {

            //statement.setInt(1, fin.getMember_id());
            statement.setFloat(1, fin.getFine_total());
            statement.setBoolean(2, fin.getFine_status());
            statement.setInt(3, fin.getBr_id());

            check_success = statement.executeUpdate() > 0;

        }
        return check_success;
    }

}
