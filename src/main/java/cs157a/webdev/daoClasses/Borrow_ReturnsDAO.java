package cs157a.webdev.daoClasses;

import cs157a.webdev.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Borrow_ReturnsDAO {
    private static String url = "jdbc:postgresql://localhost:5432/";
    private static String dbName = "lib";
    private static String dbUser = "postgres";
    private static String dbPassword = "000000";


    private static final String DELETE_BR_SQL = "DELETE FROM Borrow_Returns where br_id = ?;";
    private static final String INSERT_BR_SQL = "INSERT INTO Borrow_Returns (borrow_date, return_date, due_date, borrowed_book_status, member_id, book_id) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String JOIN_TABLES = "SELECT br.br_id, m.member_id, b.book_id, b.title, br.borrow_date, br.return_date, br.due_date, br.borrowed_book_status FROM Borrow_Returns br INNER JOIN Members m ON br.member_id = m.member_id INNER JOIN Books b ON br.book_id = b.book_id WHERE br.member_id = ?;";
    private static final String JOIN_TABLES_CHECKOUT = "SELECT br.br_id, m.member_id, b.book_id, b.title, b.available_copies, br.borrow_date, br.return_date, br.due_date, br.borrowed_book_status FROM Borrow_Returns br INNER JOIN Members m ON br.member_id = m.member_id INNER JOIN Books b ON br.book_id = b.book_id WHERE br.br_id = ?;";

    private static final String SELECT_ALL_BR = "SELECT * FROM Members";
    private static final String INSERT_MEMBERS_SQL = "INSERT INTO Members (first_name, last_name, email, membership_date) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_MEMBERS_SQL = "UPDATE Members SET first_name = ?, last_name= ?, email =? where member_id = ?;";
    //private static final String SELECT_BY_ID = "SELECT member_id, first_name, last_name, email, membership_date FROM members WHERE member_id = ?;";


    // establish connection
    public static Connection getConnection() throws SQLException {
        Connection con = null;

        try{
            Class.forName("org.postgresql.Driver");
            con= DriverManager.getConnection(url + dbName, dbUser, dbPassword);
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


    public void insertBR(Borrow_Returns br) throws SQLException {
        System.out.println(INSERT_BR_SQL);
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BR_SQL)) {
            //preparedStatement.setInt(1, mem.getMember_id()); // incremented when user fills out new form with if SERIAL?
            preparedStatement.setDate(1, br.getBorrow_date());
            preparedStatement.setDate(2, br.getReturn_date());
            preparedStatement.setDate(3, br.getDue_date());
            preparedStatement.setString(4, br.getBorrowed_book_status());
            preparedStatement.setInt(5, br.getMember_id());
            preparedStatement.setInt(6, br.getBook_id());
            //preparedStatement.setInt(8,br.getAvailable_copies());

            /*private int book_id;
            private int member_id;
            // only items we can insert
            private java.util.Date borrow_date;
            private java.util.Date return_date;
            private java.util.Date due_date;
            private String borrowed_book_status;*/

            System.out.println("Manual B/R Inserted Successfully...");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Insert B/R" + e);
        }
    }

    public boolean deleteB_R(int id) throws SQLException {
        boolean result = false;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_BR_SQL);) {
            statement.setInt(1, id);
            boolean check_success = statement.executeUpdate() > 0;
            if (check_success) {
                System.out.println("B/R deleted with id: " + id);
                result = true;
            } else
            {
                System.out.println("B/R deletion failed");
                result = false;
            }

        }

        return result;
    }

    /*public Borrow_Returns getMemberById(int id) throws SQLException {
        ResultSet resultSet = null;
        Borrow_Returns member = null;

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
*/
    /*

    public boolean updateBR(Borrow_Returns br) throws SQLException {
        boolean check_success;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_MEMBERS_SQL);) {
            statement.setString(1, br.getFirst_name());
            statement.setString(2, br.getLast_name());
            statement.setString(3, br.getEmail());
            statement.setInt(4, br.getMember_id());


            *//*private int br_id;
            private int book_id;
            private int member_id;
            // only items we can update
            private java.util.Date borrow_date;
            private java.util.Date return_date;
            private java.util.Date due_date;
            private String borrowed_book_status;*//*

            check_success = statement.executeUpdate() > 0;

        }
        return check_success;
    }

    */

    // Most likely not needed
    /*public List<Borrow_Returns> selectAllMembers() throws SQLException {

        List <Borrow_Returns> br = new ArrayList<>();
        try (Connection connection = getConnection();PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_MEMBERS);)
        {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int memberId = rs.getInt("member_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                Date date = rs.getDate("membership_date");
                br.add(new Borrow_Returns(memberId, firstName, lastName, email, date));
            }
        }
        System.out.println(mem);
        return mem;
    }

    */

    public List<Borrow_Returns> sortBRAsc(int memId) throws SQLException {
        List<Borrow_Returns> sortedBR = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(JOIN_TABLES)) {

             statement.setInt(1, memId);
             ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Borrow_Returns br = new Borrow_Returns();
                br.setBr_id(rs.getInt("br_id"));
                br.setMember_id(rs.getInt("member_id"));
                br.setBook_id(rs.getInt("book_id"));
                br.setTitle(rs.getString("title"));
                br.setBorrow_date(rs.getDate("borrow_date"));
                br.setReturn_date(rs.getDate("return_date"));
                br.setDue_date(rs.getDate("due_date"));
                br.setBorrowed_book_status(rs.getString("borrowed_book_status"));



                sortedBR.add(br);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sortedBR;
    }

    public Borrow_Returns getBrById(int id) throws SQLException {
        ResultSet resultSet = null;
        Borrow_Returns br = null;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(JOIN_TABLES_CHECKOUT);) {
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                br = new Borrow_Returns();
                br.setBr_id(resultSet.getInt("br_id"));
                br.setMember_id(resultSet.getInt("member_id"));
                br.setBook_id(resultSet.getInt("book_id"));
                br.setTitle(resultSet.getString("title"));
                br.setBorrow_date(resultSet.getDate("borrow_date"));
                br.setReturn_date(resultSet.getDate("return_date"));
                br.setDue_date(resultSet.getDate("due_date"));
                br.setBorrowed_book_status(resultSet.getString("borrowed_book_status"));
                br.setAvailable_copies(resultSet.getInt("available_copies"));

            }
        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return br;
    }

}

    /*public static void main(String[] args) throws SQLException {
        MembersDAO membersDAO = new MembersDAO();
        Members newMember = new Members();
        newMember.setMember_id(8);
        newMember.setFirst_name("Tedddster");
        newMember.setLast_name("Usddttttder1");
        newMember.setEmail("test1.ddusejknkr@example.com");
        LocalDate signupLocalDate = LocalDate.now();
        Date signupDate = Date.valueOf(signupLocalDate);
        newMember.setMembership_date(signupDate);

        // Call the insertMember method
        //membersDAO.insertMember(newMember);
        //membersDAO.deleteMember(2);


        membersDAO.selectAllMembers();

    }
*/





