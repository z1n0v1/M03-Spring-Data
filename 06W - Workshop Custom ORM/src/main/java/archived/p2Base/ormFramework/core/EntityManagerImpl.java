package archived.p2Base.ormFramework.core;

import miniOrm.anotation.Column;
import miniOrm.anotation.Entity;
import miniOrm.anotation.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EntityManagerImpl implements EntityManager {

    private final Connection connection;

    public EntityManagerImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T findById(int id, Class<T> type) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String tableName = type.getAnnotation(Entity.class).tableName();
        String idColumnName = Arrays.stream(type.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow()
                .getName();

        PreparedStatement stmt
                = this.connection.prepareStatement("SELECT * FROM " + tableName + " WHERE " + idColumnName + " = ?");

        stmt.setInt(1, id);

        T entity = (T) type.getConstructors()[0].newInstance();


        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        //ToDo: Add else if for LocalDate case
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column columnInfo = field.getAnnotation(Column.class);
                String setterName = "set" + ((field.getName().charAt(0) + "").toUpperCase()) + field.getName().substring(1);
                if (field.getType().equals(String.class)) {
                    String s = rs.getString(columnInfo.name());
                    type.getMethod(setterName, String.class).invoke(entity, s);
                } else if (field.getType().equals(LocalDate.class)) {
                    LocalDate s = LocalDate.parse(rs.getString(columnInfo.name()));
                    type.getMethod(setterName, LocalDate.class).invoke(entity, s);
                } else {
                    int s = rs.getInt(columnInfo.name());
                    type.getMethod(setterName, field.getType()).invoke(entity, s);
                }
            } else if (field.isAnnotationPresent(Id.class)) {
                String setterName = "set" + ((field.getName().charAt(0) + "").toUpperCase()) + field.getName().substring(1);
                type.getMethod(setterName, int.class).invoke(entity, id);
            }
        }


        return entity;
    }

    @Override
    public <T> boolean persist(T entity) throws IllegalAccessException, SQLException {
        Field idField = getIdFieldFromEntity(entity);
        idField.setAccessible(true);
        int id = (int) idField.get(entity);

        if (id == 0) {
            return doInsert(entity);
        }

        return doUpdate(id, entity);
    }

    @Override
    public <T> boolean delete(T entity) throws IllegalAccessException, SQLException {
        Field fieldId = getIdFieldFromEntity(entity);
        fieldId.setAccessible(true);
        int id = (int) fieldId.get(entity);

        String tableName = getTableNameByEntity(entity);

        String deleteQuery = String.format("DELETE FROM %s WHERE id = ?",
                tableName);

        PreparedStatement preparedStatement = connection
                .prepareStatement(deleteQuery);
        preparedStatement.setInt(1, id);

        return preparedStatement.execute();
    }

    @Override
    public <T> boolean alterTable(T entity) throws SQLException {
        Set<String> columnsInTable = getAllColumnsInTableBy(entity);
        return false;
    }

    private <T> Set<String> getAllColumnsInTableBy(T entity) throws SQLException {
        String tableName = getTableNameByEntity(entity);
        Set<String> allColumns = new HashSet<>();

        String query = "";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery(query);

        while (resultSet.next()) {
            //Todo: allColumns.add();
        }

        return allColumns;
    }

    private <T> boolean doUpdate(int id, T entity) throws SQLException {
        String tableName = getTableNameByEntity(entity);

        String fieldsNamesAndValues = getFieldAndValuesAsMap(entity).entrySet()
                .stream()
                .map(kvp -> String.format(" %s = %s ", kvp.getKey(), kvp.getValue()))
                .collect(Collectors.joining(", "));

        String updateQuery = String.format("UPDATE %s SET %s WHERE id = ?;",
                tableName, fieldsNamesAndValues);

        PreparedStatement preparedStatement = connection
                .prepareStatement(updateQuery);
        preparedStatement.setInt(1, id);

        return preparedStatement.execute();
    }

    private <T> Map<String, String> getFieldAndValuesAsMap(T entity) {
        Map<String, String> resultMap = new LinkedHashMap<>();

        Arrays.stream(entity
                .getClass()
                .getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    String fieldName = field.getAnnotation(Column.class).name();
                    String fieldValue = null;
                    try {
                        fieldValue = getValueToString(field, entity);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    resultMap.put(fieldName, fieldValue);
                });

        return resultMap;
    }

    private <T> boolean doInsert(T entity) throws SQLException {
        String tableName = getTableNameByEntity(entity);

        String fieldsNames = getFieldNamesBy(entity.getClass());

        String fieldValues = getFieldsValuesAsStr(entity);

        String query = String.format("INSERT INTO %s (%s) VALUES (%s) ",
                tableName, fieldsNames, fieldValues);

        PreparedStatement preparedStatement = connection
                .prepareStatement(query);

        return preparedStatement.execute();
    }

    private <T> String getFieldsValuesAsStr(T entity) {
        return Arrays.stream(entity
                .getClass()
                .getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> {
                    try {
                        return getValueToString(field, entity);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.joining(", "));
    }

    private String getFieldNamesBy(Class<?> aClass) {

        return Arrays.stream(aClass
                .getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> field.getAnnotation(Column.class).name())
                .collect(Collectors.joining(" ,"));

    }

    private <T> String getTableNameByEntity(T entity) {
        return entity
                .getClass()
                .getAnnotation(Entity.class)
                .tableName();
    }

    private <T> Field getIdFieldFromEntity(T entity) {
        return Arrays.stream(entity
                .getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Entity doesn't have id"));
    }

    private <T> String getValueToString(Field field, T entity) throws IllegalAccessException {
        field.setAccessible(true);
        String type = field.getAnnotation(Column.class).columnDefinition();

        if (type.equals("DATE") || type.startsWith("VARCHAR")) {
            try {
                return String.format(" '%s' ", field.get(entity));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return String.format(" %s ", field.get(entity));

    }
}
