package exercise;

import entities.Employee;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class T11FindEmployeesByFirstName implements Runnable {
    private final EntityManager entityManager;
    private final BufferedReader reader;

    public T11FindEmployeesByFirstName(EntityManager entityManager, BufferedReader reader) {
        this.entityManager = entityManager;
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            System.out.print("First name pattern: ");
            String pattern = reader.readLine();

            entityManager.createQuery("SELECT e FROM Employee e WHERE lower(e.firstName) LIKE lower(:pattern)",
                    Employee.class).setParameter("pattern", pattern + '%').getResultList()
                    .forEach(employee -> {
                        System.out.printf("%s %s - %s - (%s)%n",
                                employee.getFirstName(),
                                employee.getLastName(),
                                employee.getJobTitle(),
                                NumberFormat.getCurrencyInstance(Locale.US)
                                        .format(employee.getSalary()));
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
