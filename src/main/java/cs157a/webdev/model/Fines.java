package cs157a.webdev.model;

public class Fines {
    private int br_id;
    int fine_total;
    private boolean fine_status;
    int member_id;

    public boolean getFine_status() {
        return fine_status;
    }

    public void setFine_status(boolean fine_status) {
        this.fine_status = fine_status;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public Fines()
    {}

    public Fines( int br_id, int fine_total, boolean fine_status) {
        this.br_id = br_id;
        this.fine_total = fine_total;
        this.fine_status = fine_status;
    }


    public int getBr_id() {
        return br_id;
    }

    public void setBr_id(int br_id) {
        this.br_id = br_id;
    }

    public int getFine_total() {
        return fine_total;
    }

    public void setFine_total(int fine_total) {
        this.fine_total = fine_total;
    }



}
