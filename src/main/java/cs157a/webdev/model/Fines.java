package cs157a.webdev.model;

public class Fines {
    private int fine_id;
    private int br_id;
    int fine_total;
    private String fine_status;


    public Fines()
    {}

    public Fines(int fine_id, int br_id, int fine_total, String fine_status) {
        this.fine_id = fine_id;
        this.br_id = br_id;
        this.fine_total = fine_total;
        this.fine_status = fine_status;
    }

    public int getFine_id() {
        return fine_id;
    }

    public void setFine_id(int fine_id) {
        this.fine_id = fine_id;
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

    public String getFine_status() {
        return fine_status;
    }

    public void setFine_status(String fine_status) {
        this.fine_status = fine_status;
    }

}
