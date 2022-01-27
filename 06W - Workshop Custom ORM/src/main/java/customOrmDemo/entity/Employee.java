package customOrmDemo.entity;

import miniOrm.anotation.Column;
import miniOrm.anotation.Entity;
import miniOrm.anotation.Id;

@Entity(tableName = "employees")
public class Employee {

    @Id
    private long employeeId;

    @Column(name = "employee_salary", columnDefinition = "DECIMAL(19,4)")
    private double salary;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
