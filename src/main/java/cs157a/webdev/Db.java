package cs157a.webdev;

import cs157a.webdev.daoClasses.*;

public class Db {
    public static BooksDAO books;
    public static Borrow_ReturnsDAO borrows;
    public static FinesDAO fines;
    public static MembersDAO members;

    public static void init() {
        books = new BooksDAO();
        borrows = new Borrow_ReturnsDAO();
        fines = new FinesDAO();
        members = new MembersDAO();
    }
}
