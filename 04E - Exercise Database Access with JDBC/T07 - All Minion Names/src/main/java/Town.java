import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Town {
    private final int id;
    private String name, country;
    private final Connection connection;
    private boolean isNewlyInserted;

    public Town(int id, String name, String country, boolean isNewlyInserted, Connection connection) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.connection = connection;
        this.isNewlyInserted = isNewlyInserted;
    }

    public Town(String name, String country, Connection connection) throws SQLException {
        this.connection = connection;
        this.id = getId(name, country);
        this.name = name;
        this.country = country;
    }

    public Town(String name, Connection connection) throws SQLException {
        this(name, "", connection);
    }

    public int getId() {
        return id;
    }

    public void updateName(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE towns SET name = ? WHERE id = ?");
        statement.setString(1, name);
        statement.setInt(2, this.id);
        if(statement.executeUpdate() != 1)
            throw new SQLException("Error updating the name of " + this.name + " town to" + name + "at id=" + this.id);
        this.name = name;
    }

    public static List<Town> getTownsByCountry(String country, Connection connection) throws SQLException {
        List<Town> towns = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM towns WHERE country = ?");
        statement.setString(1, country);
        statement.executeQuery();
        ResultSet set = statement.getResultSet();

        while (set.next()) {
            towns.add(new Town(
                    set.getInt("id"),
                    set.getString("name"),
                    set.getString("country"),
                    false, connection));
        }
        return towns;
    }

    public String getName() {
        return name;
    }

    public boolean isNewlyInserted() {
        return isNewlyInserted;
    }

    private int getId(String name, String country) throws SQLException {

        PreparedStatement getTownStatement = connection.prepareStatement(
                "SELECT id FROM towns WHERE name = ?");
        getTownStatement.setString(1, name);
//        getTownStatement.setString(2, country);

        getTownStatement.executeQuery();
        ResultSet townResultSet = getTownStatement.getResultSet();

        if (townResultSet.next()) {
            isNewlyInserted = false;
            return townResultSet.getInt("id");
        } else {
            // add the town to DB
//            connection.setAutoCommit(false); // Start transaction
            PreparedStatement addTownToDB = connection.prepareStatement(
                    "INSERT INTO towns (name, country) VALUE (?,?)");
            addTownToDB.setString(1, name);
            addTownToDB.setString(2, country);
            addTownToDB.executeUpdate();

            Statement statement = connection.createStatement();
            statement.execute("SELECT LAST_INSERT_ID() AS id");
//            connection.commit();
//            connection.setAutoCommit(true); // End transaction

            ResultSet townIdResultSet = statement.getResultSet();
//            if (!townResultSet.next())
//                throw new SQLException("Unable to insert town" + name + "into the DB");
            isNewlyInserted = true;
            townIdResultSet.next();
            return townIdResultSet.getInt("id");
        }

    }
}
