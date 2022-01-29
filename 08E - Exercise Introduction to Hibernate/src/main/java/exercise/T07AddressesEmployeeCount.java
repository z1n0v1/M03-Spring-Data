package exercise;

import entities.Address;
import javax.persistence.EntityManager;

public class T07AddressesEmployeeCount implements Runnable {
    private final EntityManager entityManager;

    public T07AddressesEmployeeCount(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void run() {

        entityManager.getTransaction().begin();
        entityManager.createQuery
                ("SELECT a FROM Address a ORDER BY size(a.employees) DESC",
                Address.class).setMaxResults(10).getResultList().forEach(address -> {
                        System.out.printf("%s, %s - %d employees%n",
                                address.getText(),
                                null == address.getTown() ? "None" : address.getTown().getName(),
                                address.getEmployees().size());
        });

        entityManager.getTransaction().commit();
    }
}
