package orm;

import orm.anotations.Column;
import orm.anotations.Entity;
import orm.anotations.Id;
import orm.interfaces.DbContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityManager<E> implements DbContext<E> {
    private final Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    private Field getId(Class<?> entity) {
        return Arrays.stream(entity.getDeclaredFields())
                .filter(e -> e.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Entity does not have primary key"));
    }

    private String getTableFromEntity(Class<?> clazz) {
        return clazz.getAnnotation(Entity.class).name();
    }

    public boolean persist(E entity) throws IllegalAccessException, SQLException {
        Field primaryKey = getId(entity.getClass());
        primaryKey.setAccessible(true);
        Object value = primaryKey.get(entity);

        if (value == null || (long) value <= 0)
            return doInsert(entity, primaryKey);
        return doUpdate(entity, primaryKey);
    }

    private boolean doUpdate(E entity, Field primaryKey) throws IllegalAccessException, SQLException {
        String tableName = this.getTableFromEntity(entity.getClass());
        primaryKey.setAccessible(true);
        long id = (long) primaryKey.get(entity);

        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(tableName).append(" SET ");
        Map<String, Object> columnsValues = getColumnsValues(entity);

        for (String column : columnsValues.keySet()) {
            query.append(column).append("= ? ");
        }
        query.append("WHERE id = ?");
        PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
        int paramNum = 1;
        for (Object column : columnsValues.values()) {
            preparedStatement.setObject(paramNum++, column);
        }

        preparedStatement.setLong(paramNum, id);
        return 1 == preparedStatement.executeUpdate();
    }

    private boolean doInsert(E entity, Field primaryKey) throws IllegalAccessException, SQLException {
        String tableName = this.getTableFromEntity(entity.getClass());
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(tableName).append(" (");
        Map<String, Object> columnsValues = getColumnsValues(entity);

        query.append(String.join(", ", columnsValues.keySet())).append(") VALUES (");
        for (int i = 0; i < columnsValues.size(); i++) {
            query.append("?");
            if (i < columnsValues.size() - 1)
                query.append(",");
        }
        query.append(')');
        PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
        int filedNum = 1;
        for (Object value : columnsValues.values()) {
            preparedStatement.setObject(filedNum++, value);
        }
        if (1 == preparedStatement.executeUpdate()) {
            Statement statement = connection.createStatement();
            statement.execute("SELECT LAST_INSERT_ID()");
            ResultSet idResultSet = statement.getResultSet();
            idResultSet.next();
            long insertId = idResultSet.getLong(1);
            primaryKey.setAccessible(true);
            primaryKey.set(entity, insertId);
            return true;
        } else return false;
    }

    private Map<String, Object> getColumnsValues(E entity) throws IllegalAccessException {
        Map<String, Object> columnsValues = new LinkedHashMap<>();

//        Field[] fields2 = entity.getClass().

        Field[] fields = Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .toArray(Field[]::new);
        for (Field field : fields) {
            String columnName = field.getAnnotation(Column.class).name();
            field.setAccessible(true);
            Object columnValue = field.get(entity);
            columnsValues.put(columnName, columnValue);
        }
        return columnsValues;
    }



    @Override
    public Iterable<E> find(Class<E> table, String where) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String tableName = getTableFromEntity(table);

        String query = String.format("SELECT * FROM %s %s",
                tableName, where != null ? "WHERE " + where : "");
        statement.execute(query);
        ResultSet resultSet = statement.getResultSet();

        class iterableEntity implements Iterable<E> {
            private final ResultSet resultSet;
            private final Class<E> enClass;

            iterableEntity(ResultSet resultSet, Class<E> enClass) {
                this.resultSet = resultSet;
                this.enClass = enClass;
            }

            @Override
            public Iterator<E> iterator() {

                try {
                    return new EntityIterator(resultSet, enClass);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            class EntityIterator implements Iterator<E> {
                private final ResultSet resultSet;
                private final int rows;
                private int currentRow;
                private final Class<E> myTable;

                public EntityIterator(ResultSet resultSet, Class<E> table) throws SQLException {
                    this.resultSet = resultSet;
                    currentRow = 0;
                    resultSet.last();
                    rows = resultSet.getRow();
                    resultSet.beforeFirst();
                    myTable = table;
                }

                @Override
                public boolean hasNext() {
                    return rows > currentRow;
                }

                @Override
                public E next() {

                    try {
                        currentRow++;
                        E entity = table.getDeclaredConstructor().newInstance();
                        resultSet.next();
                        fillEntity(myTable, resultSet, entity);
                        return entity;

                    } catch (SQLException | InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }
        }
        return new iterableEntity(resultSet, table);
    }

    @Override
    public  Iterable<E> find(Class<E> table) throws SQLException {
        return find(table, null);
    }

    @Override
    public E findFirst(Class<E> table, String where) throws SQLException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Statement statement = connection.createStatement();
        String tableName = getTableFromEntity(table);

        String query = String.format("SELECT * FROM %s %s LIMIT 1",
                tableName, where != null ? "WHERE " + where : "");

        ResultSet resultSet = statement.executeQuery(query);
        E entity = table.getDeclaredConstructor().newInstance();

        resultSet.next();
        fillEntity(table, resultSet, entity);
        
        return entity;
    }

    @Override
    public E findFirst(Class<E> table) throws SQLException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        return findFirst(table, null);
    }

    private void fillEntity(Class<E> table, ResultSet resultSet, E entity) throws SQLException, IllegalAccessException {
        Field[] declaredFields = Arrays.stream(table.getDeclaredFields()).toArray(Field[]::new);

        for (Field field : declaredFields) {
            field.setAccessible(true);
            fillField(field, resultSet, entity);
        }
    }

    private void fillField(Field field, ResultSet resultSet, E entity) throws SQLException, IllegalAccessException {

        if (field.getType() == int.class || field.getType() == long.class) {
            field.set(entity, resultSet.getInt(field.getName()));
        } else if (field.getType() == LocalDate.class) {
            field.set(entity, LocalDate.parse(resultSet.getString(field.getName())));
        } else {
            field.set(entity, resultSet.getString(field.getName()));
        }
    }



}
