package archived.p1Base.bg.codexio.customOrmDemo.entity;

import archived.p1Base.ormFramework.annotation.Column;
import archived.p1Base.ormFramework.annotation.Entity;
import archived.p1Base.ormFramework.annotation.Id;

@Entity(tableName = "employees")
public class Employee {

    @Id
    private int employeeId;

    @Column(name = "employee_salary", columnDefinition = "DECIMAL(19,4)")
    private double salary;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
