package miniOrm.core;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface EntityManager { // CRUD
    <T> T findById(long id, Class<T> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException; // Read
    <T> T findFirst(Class<T> tClass) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException; // Read
    <T> T findFirst(Class<T> tClass, String where) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException; // Read
    <T> Iterable<T> find(Class<T> tClass) throws SQLException; // Read
    <T> Iterable<T> find(Class<T> tClass, String where) throws SQLException; // Read
    <T> boolean persist(T entity) throws IllegalAccessException, SQLException; // Create, Update
    <T> boolean delete(T entity) throws IllegalAccessException, SQLException; // Delete
}
