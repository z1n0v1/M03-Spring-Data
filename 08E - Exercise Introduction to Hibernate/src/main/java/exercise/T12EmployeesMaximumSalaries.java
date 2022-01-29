package exercise;

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

        List<Object[]> rows = entityManager.createNativeQuery(
                "SELECT MAX(e.salary) AS 'max_salary', d.name\n" +
                        "FROM employees e\n" +
                        "JOIN departments d ON d.department_id = e.department_id\n" +
                        "GROUP BY d.name\n" +
                        "HAVING max_salary NOT BETWEEN 30000 AND 70000;").getResultList();
        rows.forEach(row -> {
            System.out.printf("%s %s%n", row[1], row[0]);
        });

    }
}
