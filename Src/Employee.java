import java.util.Date;

public class Employee {
    private int code;
    private String name;
    private double salary;
    private Date hiringDate;
    private boolean active;

    public Employee(int code, String name, double salary, Date hiringDate, boolean active) {
        this.code = code;
        this.name = name;
        this.salary = salary;
        this.hiringDate = hiringDate;
        this.active = active;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public Date getHiringDate() {
        return hiringDate;
    }

    public boolean isActive() {
        return active;
    }
}
