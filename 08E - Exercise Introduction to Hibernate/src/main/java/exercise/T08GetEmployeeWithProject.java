package exercise;

import entities.Employee;
import entities.Project;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;

public class T08GetEmployeeWithProject implements Runnable {
    private final EntityManager entityManager;
    private final BufferedReader reader;

    public T08GetEmployeeWithProject(EntityManager entityManager, BufferedReader reader) {
        this.entityManager = entityManager;
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            System.out.print("employee id: ");
            int id = Integer.parseInt(reader.readLine());

            entityManager.createQuery(
                            "SELECT e FROM Employee e WHERE e.id = :id", Employee.class)
                    .setParameter("id", id).getResultList().forEach(employee -> {
                        System.out.printf("%s %s - %s%n",
                                employee.getFirstName(), employee.getLastName(), employee.getDepartment().getName());

                        employee.getProjects().stream().sorted(Comparator.comparing(Project::getName))
                                .forEach(project -> System.out.printf("\t%s%n", project.getName()));
                    });

        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
