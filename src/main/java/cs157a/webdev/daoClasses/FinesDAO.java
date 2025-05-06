package cs157a.webdev.daoClasses;

import cs157a.webdev.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinesDAO {

    private static final String DELETE_FINES_SQL = "DELETE FROM Fines where fine_id = ?;";
    // TODO... made id SERIAL choose whether to handle it or not, how to handle it
    private static final String UPDATE_FINES_SQL = "UPDATE Fines SET br_id = ?, member_id = ?, fine_total = ?, fine_status = ? where fine_id = ?;";
    private static final String SELECT_BY_ID = "SELECT * FROM Fines WHERE member_id = ? ORDER BY br_id";
    private static final String SELECT_BY_FINEID = "SELECT * FROM Fines WHERE fine_id = ?";


    private static final String SELECT_FINE_BY_BR_ID = "SELECT COUNT(*) FROM fines WHERE br_id = ?";
    private static final String INSERT_FINE = "INSERT INTO fines (br_id, fine_total, fine_status, member_id) VALUES (?, ?, ?, ?)";

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
            statement.setString(3, fine.getFine_status());
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


    public boolean deleteFines(int id) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_FINES_SQL);) {
            statement.setInt(1, id);
            boolean check_success = statement.executeUpdate() > 0;
            if (check_success) {
                System.out.println("Fine deleted with id: " + id);
            } else {
                System.out.println("Fine deletion failed");
            }

        }

        return false;
    }


    public Fines getFineById(int id) throws SQLException {
        ResultSet resultSet = null;
        Fines fine = null;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_BY_FINEID);) {
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                fine = new Fines();
                fine.setFine_id(resultSet.getInt("fine_id"));
                fine.setBr_id(resultSet.getInt("br_id"));
                fine.setFine_total(resultSet.getInt("fine_total"));
                fine.setFine_status(resultSet.getString("fine_status"));

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
                fine.setFine_id(rs.getInt("fine_id"));
                fine.setBr_id(rs.getInt("br_id"));
                fine.setFine_total(rs.getInt("fine_total"));
                fine.setFine_status(rs.getString("fine_status"));
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
            statement.setInt(1, fin.getBr_id());
            statement.setInt(3, fin.getFine_total());
            statement.setString(4, fin.getFine_status());
            statement.setInt(5, fin.getFine_id());

            check_success = statement.executeUpdate() > 0;

        }
        return check_success;
    }

}

