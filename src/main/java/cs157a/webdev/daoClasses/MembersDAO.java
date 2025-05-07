package cs157a.webdev.daoClasses;

import cs157a.webdev.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembersDAO {
    private static final String INSERT_MEMBERS_SQL = "INSERT INTO Members (first_name, last_name, email, membership_date) VALUES (?, ?, ?, ?);";
    private static final String SELECT_ALL_MEMBERS = "SELECT * FROM Members";
    private static final String UPDATE_MEMBERS_SQL = "UPDATE Members SET first_name = ?, last_name= ?, email =? where member_id = ?;";
    private static final String SORT_BY_ID = "SELECT member_id, first_name, last_name, email, membership_date FROM members ORDER BY member_id;";
    private static final String sql = "SELECT COUNT(*) FROM members WHERE member_id = ?;";
    private static final String SELECT_BY_ID = "SELECT member_id, first_name, last_name, email, membership_date FROM members WHERE member_id = ?;";
    private static final String DELETE_FINES_MEMBER_ID = "DELETE FROM Fines where member_id = ?;";
    private static final String DELETE_BR_MEMBER_ID = "DELETE FROM Borrow_returns where member_id = ?;";
    private static final String DELETE_MEMBERS_SQL = "DELETE FROM Members where member_id = ?;";


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


    public void insertMember(Members mem) throws SQLException {
        System.out.println(INSERT_MEMBERS_SQL);
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MEMBERS_SQL)) {
            //preparedStatement.setInt(1, mem.getMember_id()); // incremented when user fills out new form with if SERIAL?
            preparedStatement.setString(1, mem.getFirst_name());
            preparedStatement.setString(2, mem.getLast_name());
            preparedStatement.setString(3, mem.getEmail());
            preparedStatement.setDate(4, mem.getMembership_date());

            System.out.println("Member Inserted Successfully...");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Insert Members" + e);
        }
    }

    public boolean deleteMember(int id) throws SQLException {
        boolean result = false;
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;

        System.out.println("Member Deleted Successfully...");

        try {

            connection = getConnection();

            connection.setAutoCommit(false); // Start transaction!

            preparedStatement1 = connection.prepareStatement(DELETE_FINES_MEMBER_ID);
            preparedStatement1.setInt(1, id);
            preparedStatement1.executeUpdate();


            preparedStatement2 = connection.prepareStatement(DELETE_BR_MEMBER_ID);
            preparedStatement2.setInt(1, id);
            preparedStatement2.executeUpdate();

            preparedStatement3 = connection.prepareStatement(DELETE_MEMBERS_SQL);
            preparedStatement3.setInt(1, id);
            int check3 = preparedStatement3.executeUpdate();

            if(check3 >0){

                result = true;
                connection.commit();
            }else{
                connection.rollback();
            }


        }catch (Exception e){

        }



        return result;
    }


    public Members getMemberById(int id) throws SQLException {
        ResultSet resultSet = null;
        Members member = null;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);) {
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                member = new Members();
                member.setMember_id(resultSet.getInt("member_id"));
                member.setFirst_name(resultSet.getString("first_name"));
                member.setLast_name(resultSet.getString("last_name"));
                member.setEmail(resultSet.getString("email"));
                member.setMembership_date(resultSet.getDate("membership_date"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return member;
    }

    public boolean updateMember(Members mem) throws SQLException {
        boolean check_success;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_MEMBERS_SQL);) {
            statement.setString(1, mem.getFirst_name());
            statement.setString(2, mem.getLast_name());
            statement.setString(3, mem.getEmail());
            statement.setInt(4, mem.getMember_id());

            check_success = statement.executeUpdate() > 0;

        }
        return check_success;
    }

    public List<Members> sortMembersAsc() throws SQLException {
        List<Members> sortedMembers = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SORT_BY_ID);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                // similar to og method should check why this should be default...
                Members member = new Members();
                member.setMember_id(rs.getInt("member_id"));
                member.setFirst_name(rs.getString("first_name"));
                member.setLast_name(rs.getString("last_name"));
                member.setEmail(rs.getString("email"));
                member.setMembership_date(rs.getDate("membership_date"));
                sortedMembers.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sortedMembers;
    }

    public boolean checkMemberIdExists(int memberId) throws SQLException {
        ResultSet resultSet = null;
        boolean check_exists = false;
        try (Connection connection = getConnection();

             PreparedStatement statement = connection.prepareStatement(sql);) { // Pass SQL to prepareStatement

            statement.setInt(1, memberId);
            resultSet = statement.executeQuery();

            // move through and check for
            if (resultSet.next()) {
                int val = resultSet.getInt(1);
                check_exists = (val > 0);
            }

        } catch (SQLException e)
        {
            System.out.println(e);
        }        return check_exists;
    }

}
