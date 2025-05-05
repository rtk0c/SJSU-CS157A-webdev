package cs157a.webdev.daoClasses;

public class DbCreds {
    // Example DB configuration, the same values as in setup.sql
    // Consider changing in a real-world deployment
    // NOTE(rtk0c): see why am I even writing this comment because this stupid piece of code is a waste of every second spent on it (humans and LLM alike)
    static String url;
    static String dbName;
    static String dbUser;
    static String dbPassword;

    static {
        url = or(System.getenv("CS157A_WEBDEV_DB_URL"), "jdbc:postgresql://localhost:5432/");
        dbName = or(System.getenv("CS157A_WEBDEV_DB_NAME"), "cs157a_webdev_db");
        dbUser = or(System.getenv("CS157A_WEBDEV_DB_USER"), "dbuser");
        dbPassword = or(System.getenv("CS157A_WEBDEV_DB_PASSWORD"), "password");
    }

    private static String or(String a, String b) {
        return a == null || a.isEmpty() ? b : a;
    }
}
