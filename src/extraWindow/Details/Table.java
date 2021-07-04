package extraWindow.Details;

import java.util.Date;

public class Table {
    private int id;
    private String category;
    private String note;
    private double sum;
    private Date date;

    public Table(int id, String category, String note, double sum, Date date) {
        this.id = id;
        this.category = category;
        this.note = note;
        this.sum = sum;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
