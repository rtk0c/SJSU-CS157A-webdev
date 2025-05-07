package cs157a.webdev.model;

import java.sql.Date;

public class Members {
    private int member_id;
    private String first_name;
    private String last_name;
    private String email;
    private Date membership_date;

    public Members(int member_id, String first_name, String last_name, String email, Date membership_date) {
        this.member_id = member_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.membership_date = membership_date;
    }

    public Members() {

    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getMembership_date() {
        return membership_date;
    }

    public void setMembership_date(Date membership_date) {
        this.membership_date = membership_date;
    }
}
