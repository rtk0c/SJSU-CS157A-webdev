package cs157a.webdev.daoClasses;

public class DbCreds {
    // Example DB configuration, the same values as in setup.sql
    // Consider changing in a real-world deployment
    // NOTE(rtk0c): see why am I even writing this comment because this stupid piece of code is a waste of every second spent on it (humans and LLM alike)
    static String url = "jdbc:postgresql://localhost:5432/";
    static String dbName = "cs157_webdev_db";
    static String dbUser = "dbuser";
    static String dbPassword = "password";
}
