package exercise;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class T04EmployeesWithSalaryOver implements Runnable{
    private final EntityManager entityManager;

    public T04EmployeesWithSalaryOver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void run() {
        // No need for transaction on select
//        entityManager.getTransaction().begin();

        entityManager.createQuery(
                "select e.firstName from Employee e WHERE e.salary > :salary", String.class)
                .setParameter("salary", new BigDecimal(50000))
                .getResultList().forEach(System.out::println);

//        entityManager.getTransaction().commit();
    }
}
