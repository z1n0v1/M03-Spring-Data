package archived.mine;

import archived.mine.entities.User;
//import entities.User;
import orm.EntityManager;
import orm.MyConnector;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, InstantiationException {
        MyConnector.createConnection("soft_uni","your_password","users_test");
        Connection connection = MyConnector.getConnection();

        EntityManager<User> entityManager = new EntityManager<>(connection);

//        User user = new User("gosho","poznaime", 13,
//                LocalDate.of(2022, 1, 1));
//        entityManager.persist(user);

        User found = entityManager.findFirst(User.class, "username = 'gosho'");

        System.out.println(found);

        for (User user1 : entityManager.find(User.class, "username = 'gosho'")) {
            System.out.println(user1);
        }

    }
}
