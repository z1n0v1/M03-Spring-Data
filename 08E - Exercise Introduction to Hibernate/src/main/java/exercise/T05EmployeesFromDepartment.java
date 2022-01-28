package exercise;

import entities.Employee;

import javax.persistence.EntityManager;

public class T05EmployeesFromDepartment implements Runnable{
    private final EntityManager entityManager;

    public T05EmployeesFromDepartment(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void run() {

        // Why does .getResultStream return different result ?

        entityManager.createQuery
                ("select e from Employee e where e.department.name = :d_name " +
                        "order by e.salary, e.id", Employee.class)
                .setParameter("d_name", "Research and Development")
                .getResultList().forEach(employee -> {
                    System.out.printf("%s %s from %s - $%.2f%n",
                            employee.getFirstName(), employee.getLastName(),
                            employee.getDepartment().getName(), employee.getSalary());
                });
    }
}
