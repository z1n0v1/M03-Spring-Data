package customOrmDemo;

import customOrmDemo.entity.User;
import miniOrm.core.EntityManager;
import miniOrm.core.EntityManagerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Iterator;

public class ApplicationStarter {
    public static void main(String[] args) throws SQLException, URISyntaxException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        EntityManager entityManager = EntityManagerFactory.create(
                "mysql",
                "localhost",
                3306,
                "softuni",
                "",
                "test_orm",
                ApplicationStarter.class
        );

        User user = new User("Pesho1", 17);
        User maria = new User("Maria", 25);

        User gosho = new User("Gosho", 33);

        entityManager.persist(maria);
        entityManager.persist(user);
        entityManager.persist(gosho);

        User pesho = entityManager.findById(1, User.class);
         User maria2 = entityManager.findById(3, User.class);
        pesho.setAge(30);
        entityManager.persist(user);

        for (User user1 : entityManager.find(User.class)) {
            System.out.println(user1.getId() + " " + user1.getAge() + " " + user1.getUsername()
            + " " + user1.getRegistration());

        }

//
//        Address softUniAddress = entityManager.findById(1, Address.class);
//        Address codexioAddress = entityManager.findById(2, Address.class);
//        Department byId1 = entityManager.findById(30, Department.class);


    }
}
