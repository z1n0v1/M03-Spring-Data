package exercise;

import entities.Address;
import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;

public class T13RemoveTowns implements Runnable {
    private final EntityManager entityManager;
    private final BufferedReader reader;

    public T13RemoveTowns(EntityManager entityManager, BufferedReader reader) {
        this.entityManager = entityManager;
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            String townName = reader.readLine();

            entityManager.getTransaction().begin();

            Town town = entityManager.createQuery("SELECT t FROM Town t WHERE t.name = :name", Town.class)
                    .setParameter("name", townName).getSingleResult();

            // The need to manually remove addresses and employees could be replaced
            // with cascade remove annotation on the relevant entities (Address, Town)
            removeAddressesByTownId(town.getId());
            entityManager.remove(town);

            entityManager.getTransaction().commit();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void removeAddressesByTownId(Integer id) {
        entityManager.createQuery("SELECT a FROM Address a WHERE a.town.id = :town_id", Address.class)
                .setParameter("town_id", id).getResultList().forEach(address -> {
                    removeEmployeesByAddressId(address.getId());
                    entityManager.remove(address);
                });
    }

    private void removeEmployeesByAddressId(Integer id) {
        entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.address.id = :address_id", Employee.class)
                .setParameter("address_id", id).getResultList().forEach(entityManager::remove);
    }
}
