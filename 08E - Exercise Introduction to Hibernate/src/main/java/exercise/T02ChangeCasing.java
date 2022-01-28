package exercise;

import javax.persistence.EntityManager;

public class T02ChangeCasing implements Runnable {
    private final EntityManager entityManager;

    public T02ChangeCasing(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void run() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("update Town t set t.name = upper(t.name) where length(t.name) <= 5")
                .executeUpdate();
        entityManager.getTransaction().commit();
    }
}
