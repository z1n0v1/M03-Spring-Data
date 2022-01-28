package exercise;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;

public class T03ContainsEmployee implements Runnable {
    private final EntityManager entityManager;
    private final BufferedReader reader;

    public T03ContainsEmployee(EntityManager entityManager, BufferedReader reader) {
        this.entityManager = entityManager;
        this.reader = reader;
    }

    @Override
    public void run() {

        try {
            System.out.print("Name: ");
            String name = reader.readLine();

            // There is no need for transaction on select
//            entityManager.getTransaction().begin();

            Long count = entityManager.createQuery("select count(e.id) from Employee e " +
                            "where concat(e.firstName,' ', e.lastName) = :name", Long.class)
                    .setParameter("name", name).getSingleResult();
            if (count == 1) System.out.println("Yes");
            else System.out.println("No");

            // Another solution could be to select Employee with the given name and check if
            // getSingleResult throws NoResultException, or getResultList().isEmpty

//            entityManager.getTransaction().commit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
