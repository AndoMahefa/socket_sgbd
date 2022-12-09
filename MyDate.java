package variable;

import java.io.Serializable;

public class MyDate implements Serializable {
    private int day, month, year;
    public MyDate(String date) {
        String[] values = date.split("-");
        day = Integer.valueOf(values[0]);
        month = Integer.valueOf(values[1]);
        year = Integer.valueOf(values[2]);
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public String toString() {
        return (day + "-" + month + "-" + year);
    }
    public boolean date_egales(MyDate date) {
        return(year == date.year && month == date.month && day == date.day);
    }
}
