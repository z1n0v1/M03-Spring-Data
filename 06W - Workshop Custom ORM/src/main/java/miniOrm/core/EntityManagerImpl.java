package miniOrm.core;

import miniOrm.anotation.Column;
import miniOrm.anotation.Entity;
import miniOrm.anotation.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

public class EntityManagerImpl implements EntityManager {
    private final Connection connection;

    public EntityManagerImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> T findById(long id, Class<T> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {

        T entity = tClass.getDeclaredConstructor().newInstance();

        String tableName = getTableNameFromEntity(entity);
        Field idField = getIdFieldFromEntity(entity);
        Field[] columns = getColumns(entity);

        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM " + tableName + " WHERE " + idField.getName() + " = ?");
        statement.setLong(1, id);
        statement.executeQuery();

        ResultSet set = statement.getResultSet();
        if(!set.next())
            throw new IllegalArgumentException("Can not find class " + tClass.getName() +
                    " in table " + tableName + " with id " + id);

        setEntityColumns(entity, columns, set);
        idField.setAccessible(true);
        idField.set(entity,set.getLong(idField.getName()));
        return entity;
    }

    private <T> void setEntityColumns(T entity, Field[] columns, ResultSet set)
            throws IllegalAccessException, SQLException {
        for (Field column : columns) {
            column.setAccessible(true);
            if (column.getType().equals(boolean.class) || column.getType().equals(Boolean.class)) {
                column.set(entity, set.getBoolean(getColumnName(column)));
            } else if (column.getType().equals(int.class) || column.getType().equals(Integer.class)) {
                column.set(entity, set.getInt(getColumnName(column)));
            } else if (column.getType().equals(long.class) || column.getType().equals(Long.class)) {
                column.set(entity, set.getLong(getColumnName(column)));
            } else if (column.getType().equals(BigDecimal.class)) {
                column.set(entity, set.getBigDecimal(getColumnName(column)));
            } else if (column.getType().equals(LocalDate.class)) {
                column.set(entity,
                        Instant.ofEpochMilli(set.getDate(getColumnName(column)).getTime())
                                .atZone(ZoneId.systemDefault()).toLocalDate()
                );
            } else if (column.getType().equals(Date.class)) {
                column.set(entity, set.getDate(getColumnName(column)));
            } else if (column.getType().equals(String.class)) {
                column.set(entity, set.getString(getColumnName(column)));
            } else throw new UnsupportedOperationException("Unsupported column type " + column.getType().getName());
        }
    }

    String getColumnName(Field column) {
        return column.getAnnotation(Column.class).name();
    }

    @Override
    public <T> T findFirst(Class<T> tClass) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return findFirst(tClass, null);
    }

    @Override
    public <T> T findFirst(Class<T> tClass, String where) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {

        T entity = tClass.getDeclaredConstructor().newInstance();

        String tableName = getTableNameFromEntity(entity);
        Field idField = getIdFieldFromEntity(entity);
        Field[] columns = getColumns(entity);

        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM " + tableName + (where != null ? " WHERE " + where : "") + " LIMIT 1");
        ResultSet set = statement.getResultSet();
        set.next();
        setEntityColumns(entity, columns, set);

        idField.setAccessible(true);
        idField.set(entity,set.getLong(idField.getName()));

        return entity;
    }

    @Override
    public <T> Iterable<T> find(Class<T> tClass) throws SQLException {
        return find(tClass, null);
    }

    @Override
    public <T> Iterable<T> find(Class<T> tClass, String where) throws SQLException {
        String tableName = tClass.getAnnotation(Entity.class).tableName();
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.execute("SELECT * FROM " + tableName + (where != null ? " WHERE " + where : ""));
        ResultSet set = statement.getResultSet();
        return getIterableFromSet(set, tClass);
    }

    private <T> Iterable<T> getIterableFromSet(ResultSet set, Class<T> tClass) {

        class IterableEntity implements Iterable<T> {
            private final ResultSet resultSet;
            private final Class<T> enClass;

            IterableEntity(ResultSet resultSet, Class<T> enClass) {
                this.resultSet = resultSet;
                this.enClass = enClass;
            }

            @Override
            public Iterator<T> iterator() {

                try {
                    return new EntityIterator(resultSet, enClass);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            class EntityIterator implements Iterator<T> {
                private final ResultSet resultSet;
                private final int rows;
                private int currentRow;
                private final Class<T> myTable;

                public EntityIterator(ResultSet resultSet, Class<T> table) throws SQLException {
                    this.resultSet = resultSet;
                    currentRow = 0;
                    resultSet.last();
                    rows = resultSet.getRow();
                    resultSet.beforeFirst();
                    myTable = table;
                    System.out.printf("%nRows: %d, Class: %s%n", rows, myTable.getSimpleName());
                }

                @Override
                public boolean hasNext() {
                    return rows > currentRow;
                }

                @Override
                public T next() {

                    try {
                        currentRow++;
                        T entity = myTable.getDeclaredConstructor().newInstance();
                        resultSet.next();
                        Field[] columns = getColumns(entity);
//                        fillEntity(myTable, resultSet, entity);
                        setEntityColumns(entity,columns, resultSet);

                        Field idField = getIdFieldFromEntity(entity);
                        idField.setAccessible(true);
                        idField.set(entity,set.getLong(idField.getName()));

                        return entity;

                    } catch (SQLException | InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }

                }
            }
        }

        return new IterableEntity(set, tClass);
    }

    @Override
    public <T> boolean persist(T entity) throws IllegalAccessException, SQLException {
        Class<?> tClass = entity.getClass();

        Field idField = getIdFieldFromEntity(entity);
        idField.setAccessible(true);
        long id = (long) idField.get(entity);

        if (id == 0)
            return doInsert(entity);
        return doUpdate(entity, id);

    }

    private <T> boolean doUpdate(T entity, long id) throws SQLException, IllegalAccessException {
        String tableName = getTableNameFromEntity(entity);
        Field[] columns = getColumns(entity);

        String query = "UPDATE " + tableName + " SET " +
                Arrays.stream(columns).map(column -> String.format("%s = ?",
                        column.getAnnotation(Column.class).name())).collect(Collectors.joining(", "))
                + " WHERE " + entity.getClass().getSimpleName() + " = ?";

        PreparedStatement statement = connection.prepareStatement(query);

        int columnNum = 1;
        for (Field column : columns)
            statement.setObject(columnNum++, column.get(entity));
        statement.setLong(columnNum, id);

        return 1 == statement.executeUpdate();
    }

    private <T> boolean doInsert(T entity) throws SQLException, IllegalAccessException {
        String tableName = getTableNameFromEntity(entity);
        Field[] columns = getColumns(entity);

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(tableName).append("(");
        query.append(
                Arrays.stream(columns).map(field -> field.getAnnotation(Column.class).name())
                        .collect(Collectors.joining(", "))
        ).append(") VALUES (");
        for (int i = 0; i < columns.length; i++) {
            query.append('?');
            if (i < columns.length - 1)
                query.append(',');
        }
        query.append(')');
//        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query.toString());
        int fildNum = 1;
        for (Field field : columns) {
            field.setAccessible(true);
            statement.setObject(fildNum++, field.get(entity));
        }
        return 1 == statement.executeUpdate();

    }

    private <T> Field[] getColumns(T entity) {

        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .toArray(Field[]::new);
    }

    private <T> String getTableNameFromEntity(T entity) {
        return entity.getClass().getAnnotation(Entity.class).tableName();
    }

    @Override
    public <T> boolean delete(T entity) throws IllegalAccessException, SQLException {
        Field idField = getIdFieldFromEntity(entity);
        idField.setAccessible(true);
        long id = (long) idField.get(entity);
        String tableName = getTableNameFromEntity(entity);

        if (id == 0)
            throw new UnsupportedOperationException("Unable to delete element without set id");
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM " + tableName + " WHERE " + idField.getName() + " = ?");
        statement.setLong(1, id);

        return 1 == statement.executeUpdate();
    }

    private <T> Field getIdFieldFromEntity(T entity) {
        return Arrays.stream(entity
                        .getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Entity doesn't have id"));
    }

}
