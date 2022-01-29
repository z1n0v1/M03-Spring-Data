package exercise;

import entities.Department;
import entities.Employee;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class T12EmployeesMaximumSalaries implements Runnable {
    private final EntityManager entityManager;

    public T12EmployeesMaximumSalaries(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {

        // Using native query

        List<Object[]> rows = entityManager.createNativeQuery(
                "SELECT MAX(e.salary) AS 'max_salary', d.name\n" +
                        "FROM employees e\n" +
                        "JOIN departments d ON d.department_id = e.department_id\n" +
                        "GROUP BY d.name\n" +
                        "HAVING max_salary NOT BETWEEN 30000 AND 70000;").getResultList();
        rows.forEach(row -> {
            System.out.printf("%s %s%n", row[1], row[0]);
        });


        /*  Using java code

        entityManager.getTransaction().begin();
        entityManager.createQuery("SELECT d FROM Department d", Department.class)
                .getResultStream().forEach(department -> {
                    BigDecimal maxSalary = department.getEmployees().stream().map(Employee::getSalary)
                            .max(BigDecimal::compareTo).orElse(null);
                    if (maxSalary != null && (maxSalary.compareTo(BigDecimal.valueOf(30000)) == -1 || // Less than 30000
                    maxSalary.compareTo(BigDecimal.valueOf(70000)) == 1)) { // more than 70000
                        System.out.printf("%s %s%n", department.getName(), maxSalary);
                    }
                });
        entityManager.getTransaction().commit();
        */

    }
}
