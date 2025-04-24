package com.daoClasses;

import com.model.Fines;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinesDAO {

    private static String url = "jdbc:postgresql://localhost:5432/";
    private static String dbName = "lib";
    private static String dbUser = "postgres";
    private static String dbPassword = "000000";


    private static final String INSERT_FINES_SQL = "INSERT INTO Fines (fine_id, br_id, member_id, fine_total, fine_status) VALUES (?, ?, ?, ?, ?);";
    private static final String SELECT_ALL_FINES = "SELECT * FROM Fines";
    private static final String DELETE_FINES_SQL = "DELETE FROM Fines where fine_id = ?;";
    // TODO... made id SERIAL choose whether to handle it or not, how to handle it
    private static final String UPDATE_FINES_SQL = "UPDATE Fines SET br_id = ?, member_id = ?, fine_total = ?, fine_status = ? where fine_id = ?;";


    // establish connection
    public static Connection getConnection() throws SQLException {
        Connection con = null;

        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url + dbName, dbUser, dbPassword);
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


    public List<Fines> selectAllFines() throws SQLException {

        List <Fines> fin = new ArrayList<>();
        try (Connection connection = getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FINES);) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int fineId = rs.getInt("fine_id");
                int brId = rs.getInt("br_id");
                int memberId = rs.getInt("member_id");
                float fineTotal = rs.getFloat("fine_total");
                String fineStatus = rs.getString("fine_status");

                fin.add(new Fines(fineId, brId, memberId, fineTotal, fineStatus));
                //  (int fine_id, int br_id, float fine_total, String fine_status)
            }
        }

        return fin;
    }


    public void deleteFines(int id) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_FINES_SQL);) {
            statement.setInt(1, id);
            boolean check_success = statement.executeUpdate() > 0;
            if (check_success) {
                System.out.println("Fine deleted with id: " + id);
            } else
            {
                System.out.println("Fine deletion failed");
            }

        }

    }


    public void insertFine(Fines fin) throws SQLException
    {

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FINES_SQL);) {
            preparedStatement.setInt(1, fin.getFine_id());
            preparedStatement.setInt(2, fin.getBr_id());
            preparedStatement.setInt(3, fin.getMember_id());
            preparedStatement.setFloat(4, fin.getFine_total());
            preparedStatement.setString(5, fin.getFine_status());
            preparedStatement.executeUpdate();
            //  int fine_id, int br_id, int member_id, float fine_total, String fine_status
            System.out.println("Fine Inserted Successfully...");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Insert Fine" + e);
        }
    }


    public boolean updateFine(Fines fin) throws SQLException {
        boolean check_success;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_FINES_SQL);) {
            statement.setInt(1, fin.getBr_id());
            statement.setInt(2, fin.getMember_id());
            statement.setFloat(3, fin.getFine_total());
            statement.setString(4, fin.getFine_status());
            statement.setInt(5, fin.getFine_id());

            check_success = statement.executeUpdate() > 0;

        }
        return check_success;
    }



// Testing methods
    public static void main(String[] args) throws SQLException {
        FinesDAO finesDAO = new FinesDAO();
        /*Fines newFine = new Fines();
        newFine.setFine_id(3);
        newFine.setBr_id(5);
        newFine.setMember_id(3);
        newFine.setFine_total(14.99F);
        newFine.setFine_status("Paid");*/

        // Call the insertMember method
        finesDAO.selectAllFines();

    }


}

