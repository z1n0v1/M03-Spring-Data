package miniOrm.core;

import miniOrm.anotation.Column;
import miniOrm.anotation.Entity;
import miniOrm.anotation.Id;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Creates new EntityManager object
 * Automatically initializes or alters tables (when there are missing fields)
 */

public class EntityManagerFactory {

    public static EntityManager create(
            String dbType,
            String host,
            int port,
            String user,
            String pass,
            String dbName,
            Class<?> mainClass
    ) throws SQLException, URISyntaxException, ClassNotFoundException {
        Connection connection = createConnection(dbType, host, port, user, pass, dbName);

        List<Class<?>> classes = getEntities(mainClass);

        prepareTables(connection, classes);

        return new EntityManagerImpl(connection);
    }

    private static void prepareTables(Connection connection, List<Class<?>> classes) throws SQLException {
        Statement statement = connection.createStatement();
        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(Entity.class)) continue;
            String tableName = clazz.getAnnotation(Entity.class).tableName();

            Map<String, String> columns = new LinkedHashMap<>();

            String idColumnName = null;
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    idColumnName = field.getName();
                } else if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    columns.put(column.name(), column.columnDefinition());
                }
            }
            if (idColumnName == null)
                throw new UnsupportedOperationException("Missing Id annotation for class " + clazz.getName());
            if (columns.isEmpty())
                throw new UnsupportedOperationException("Missing field annotations for class " + clazz.getName());
            if (tableExists(tableName, connection))
                alterToSpec(tableName, idColumnName, columns, connection);
            else createTable(tableName, idColumnName, columns, connection);
        }
    }

    private static void createTable(String tableName, String idColumnName,
                                    Map<String, String> columns, Connection connection) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE ").append(tableName).append(" (\n");
        query.append(idColumnName).append(" BIGINT PRIMARY KEY AUTO_INCREMENT,\n");
        query.append(
                columns.entrySet().stream()
                        .map(column -> String.format("%s %s", column.getKey(), column.getValue()))
                        .collect(Collectors.joining(",\n"))).append(")");
//        System.out.println(query);

        connection.createStatement().execute(query.toString());

    }

    private static void alterToSpec(String tableName, String idColumnName,
                                    Map<String, String> columns, Connection connection) throws SQLException {
        List<String> columnNames = new ArrayList<>();

        ResultSet dbColumnNames = connection.createStatement().executeQuery("DESCRIBE " + tableName);
        while (dbColumnNames.next()) {
            columns.remove(dbColumnNames.getString(1));
        }
        if (columns.isEmpty()) return;

        String query = "ALTER TABLE " + tableName + " ADD " +
                columns.entrySet().stream().map(column -> String.format("%s %s",
                        column.getKey(), column.getValue())).collect(Collectors.joining(", "));
//        System.out.println(query);
        connection.createStatement().execute(query);
    }

    private static boolean tableExists(String tableName, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement = connection.prepareStatement("SHOW TABLES LIKE ?");
        preparedStatement.setString(1, tableName);
        return preparedStatement.executeQuery().next();
    }

    private static Connection createConnection(String dbType, String host, int port, String user, String pass, String dbName) throws SQLException {
        return DriverManager.getConnection(
                "jdbc:" + dbType + "://" + host + ":" + port + "/" + dbName,
                user,
                pass
        );
    }

    private static List<Class<?>> getEntities(Class<?> mainClass) throws URISyntaxException, ClassNotFoundException {
        String path = mainClass.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String packageName = mainClass.getPackageName();


        File rootDir = new File(path + packageName.replace(".", "/"));
        List<Class<?>> classes = new ArrayList<>();

        scanEntities(
                rootDir,
                packageName,
                classes
        );
        return classes;
    }

    private static void scanEntities(File dir, String packageName, List<Class<?>> classes) throws ClassNotFoundException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanEntities(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                Class<?> classInfo = Class.forName(packageName + "." + file.getName().replace(".class", ""));
                if (classInfo.isAnnotationPresent(Entity.class)) {
                    classes.add(classInfo);
                }
            }
        }
    }
}
