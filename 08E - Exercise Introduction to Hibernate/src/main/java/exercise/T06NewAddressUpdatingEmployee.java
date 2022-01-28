package exercise;

import entities.Address;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;

public class T06NewAddressUpdatingEmployee implements Runnable {
    private final EntityManager entityManager;
    private final BufferedReader reader;

    public T06NewAddressUpdatingEmployee(EntityManager entityManager, BufferedReader reader) {
        this.entityManager = entityManager;
        this.reader = reader;
    }

    @Override
    public void run() {


        System.out.print("last name: ");
        try {
            String lastName = reader.readLine();

            Address address = new Address();
            address.setText("Vitoshka 15");

            entityManager.getTransaction().begin();

            entityManager.persist(address);

            entityManager.createQuery("UPDATE Employee e SET e.address = :address " +
                    "WHERE e.lastName = :last_name").setParameter("address", address)
                    .setParameter("last_name", lastName).executeUpdate();
            entityManager.getTransaction().commit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
