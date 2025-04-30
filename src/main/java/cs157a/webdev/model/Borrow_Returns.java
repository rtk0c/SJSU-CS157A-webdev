package cs157a.webdev.model;

import java.util.Date;

//TODO... Difference for return and due date
public class Borrow_Returns {
    private int br_id;
    private int book_id;
    private int member_id;
    private Date borrow_date;
    private Date return_date;
    private Date due_date;
    private String borrowed_book_status;

    public Borrow_Returns(int br_id, int book_id, int member_id, Date borrow_date, Date return_date, Date due_date, String borrowed_book_status) {
        this.br_id = br_id;
        //this.book_id = book_id;
        //this.member_id = member_id;
        this.borrow_date = borrow_date;
        this.return_date = return_date;
        this.due_date = due_date;
        this.borrowed_book_status = borrowed_book_status;
    }

    public int getBr_id() {
        return br_id;
    }

    public void setBr_id(int br_id) {
        this.br_id = br_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public Date getBorrow_date() {
        return borrow_date;
    }

    public void setBorrow_date(Date borrow_date) {
        this.borrow_date = borrow_date;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public String getBorrowed_book_status() {
        return borrowed_book_status;
    }

    public void setBorrowed_book_status(String borrowed_book_status) {
        this.borrowed_book_status = borrowed_book_status;
    }
}
