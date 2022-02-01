import entity.Continent;
import entity.Country;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManager entityManager = Persistence.createEntityManagerFactory(
                "e10_t06_football_betting").createEntityManager();


        entityManager.getTransaction().begin();

        //Test Continent class

        Continent continent = new Continent();
        continent.setName("Europe");
        entityManager.persist(continent);

        // Test country class

        Country country1 = new Country(), country2 = new Country();
        country1.setName("Bulgaria");
        country1.setId("BUL");
        country1.setContinent(continent);
        country2.setName("Germany");
        country2.setId("GER");
        country2.setContinent(continent);

        entityManager.persist(country1);
        entityManager.persist(country2);



        entityManager.getTransaction().commit();
    }
}
