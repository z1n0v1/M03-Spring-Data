import entities.Person;
import entities.Student;
import entities.UsersEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Playing with Hibernate

        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        Person person = new Person("Pesho", 42);

        List<Person> people = new ArrayList<>();
        people.add(new Person("Vancho", 11));
        people.add(new Person("Chocho", 13));
        people.add(new Person("Iancho", 69));
        session.saveOrUpdate(person);

        for (Person person1 : people)
            session.saveOrUpdate(person1);

        session.getTransaction().commit();

        Person personFromDB = session.get(Person.class, 3);
        System.out.println(personFromDB);

        session.beginTransaction();

        List<Person> people2 =
                session.createQuery("FROM Person ", Person.class).list();
        System.out.println("DB people dump:");
        for (Person anotherPerson : people2)
            System.out.println(anotherPerson);
        session.getTransaction().commit();

        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = builder.createQuery();
        Root<Person> root = criteriaQuery.from(Person.class);

        criteriaQuery.select(root).where(builder.like(root.get("name"), "%esho%"));

        List<Person> personList = session.createQuery(criteriaQuery).getResultList();
        System.out.println("By esho:");
        for (Person person1 : personList)
            System.out.println(person1);
        session.close();

        // Playing with JPA

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("school");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Student student = new Student("Ivo");
        em.persist(student);
        em.getTransaction().commit();

        em.getTransaction().begin();
        //after remove throws java.lang.IllegalArgumentException:
        //          attempt to create delete event with null entity
        // We have a hole in the db Ids :)
//        Student student1 = em.find(Student.class, 1L);
//        System.out.println(student1);
//        em.remove(student1);

        Student student2 = em.find(Student.class, 2L);
        System.out.println("(after find) student2 is managed: " + em.contains(student2));
        em.getTransaction().commit();
        em.close(); // and detach student2
        em = emf.createEntityManager();
        em.getTransaction().begin();
        System.out.println("(after begin) student2 is managed: " + em.contains(student2));
        student2.setName("Iva");
        em.merge(student2);
        System.out.println("(after merge) student2 id: " + student2.getId());
//        em.detach(student2);
//        em.merge(student2);
        System.out.println("student2 is managed: " + em.contains(student2));
        em.getTransaction().commit();
        System.out.println(student2.getId());
        System.out.println("(after commit) student2 is managed: " + em.contains(student2));
        System.out.println("try merging new entity");
        Student student3 = new Student("Gosho");
        em.getTransaction().begin();
        em.merge(student3);
        em.getTransaction().commit();

        // Conclusion :

/* from: https://stackoverflow.com/questions/912659/what-is-the-proper-way-to-re-attach-detached-objects-in-hibernate

So it seems that there is no way to reattach a stale detached entity in JPA.
merge() will push the stale state to the DB, and overwrite any intervening updates.
refresh() cannot be called on a detached entity.

lock() cannot be called on a detached entity, and even if it could, and it did reattach the entity,
calling 'lock' with argument 'LockMode.NONE' implying that you are locking, but not locking, is the most
counterintuitive piece of API design I've ever seen.

So you are stuck. There's an detach() method, but no attach() or reattach().
An obvious step in the object lifecycle is not available to you.

Judging by the number of similar questions about JPA, it seems that even if JPA does claim to have
a coherent model, it most certainly does not match the mental model of most programmers,
who have been cursed to waste many hours trying understand how to get JPA to do the simplest things,
and end up with cache management code all over their applications.

It seems the only way to do it is discard your stale detached entity and do a find query with the same id,
that will hit the L2 or the DB.
*/

        em.close();


    }
}
