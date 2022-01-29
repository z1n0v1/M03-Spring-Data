package exercise;

import entities.Employee;

import javax.persistence.EntityManager;
import java.text.NumberFormat;
import java.util.*;

public class T10IncreaseSalaries implements Runnable {
    private final EntityManager entityManager;

    public T10IncreaseSalaries(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void run() {
        entityManager.getTransaction().begin();


        Set<String> departmentNames = Set.of(
                "Engineering", "Tool Design", "Marketing", "Information Services"
        );

        // Update by name doesn't work. Why ?
//        entityManager.createQuery("UPDATE Employee e SET e.salary = e.salary * 1.12" +
//                        " WHERE e.department.name IN (:dep_names)").setParameter("dep_names", departmentNames)
//                .executeUpdate();

        int numAffectedColumns = entityManager.createQuery("UPDATE Employee e SET e.salary = e.salary * 1.12 " +
                "WHERE e.department.id IN :ids").setParameter("ids", Set.of(1,2,4,11)).executeUpdate();
        System.out.println(numAffectedColumns);

        entityManager.createQuery("SELECT e FROM Employee e WHERE e.department.name IN (:dep_names)", Employee.class)
                .setParameter("dep_names", departmentNames).getResultList().forEach(employee -> {
                    System.out.printf("%s %s (%s)%n", employee.getFirstName(), employee.getLastName(),
                            NumberFormat.getCurrencyInstance(Locale.US).format(employee.getSalary()));
                });

        entityManager.getTransaction().commit();
//        entityManager.createQuery("UPDATE Employee e SET e.salary = e.salary * 1.12" +
//                " WHERE e.department.name IN (" +
//                "'Engineering', 'Tool Design', 'Marketing', 'Information Services')").executeUpdate();
//        entityManager.createQuery("SELECT e FROM Employee e WHERE e.department.name IN (" +
//                "'Engineering', 'Tool Design', 'Marketing', 'Information Services')", Employee.class).getResultList()
//                .forEach(employee -> {
//                    System.out.printf("%s %s (%s)", employee.getFirstName(), employee.getLastName(),
//                            NumberFormat.getCurrencyInstance(Locale.US).format(employee.getSalary()));
//
//                });
    }
}
