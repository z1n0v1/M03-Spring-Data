import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Villain {
    private final int id;
    private final String name;
    private final Connection connection;
    private final List<Minion> minions;
    private boolean isNewlyInserted;

    public Villain(int id, Connection connection) throws SQLException {
        this.id = id;
        this.connection = connection;
        this.isNewlyInserted = false;

        PreparedStatement statement = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");
        statement.setInt(1, id);
        statement.executeQuery();

        ResultSet set = statement.getResultSet();
        if (!set.next())
            throw new IllegalArgumentException("No villain with ID " + this.id + " exists in the database.");
        this.name = set.getString("name");

        minions = new ArrayList<>();
        loadMinions();
    }

    public Villain(String name, Connection connection) throws SQLException {
        this.name = name;
        this.connection = connection;
        this.id = getIdFromDB(name);
        minions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public boolean isNewlyInserted() {
        return isNewlyInserted;
    }

    private int getIdFromDB(String name) throws SQLException {

        PreparedStatement getVillainStatement =
                connection.prepareStatement("SELECT id FROM villains WHERE name = ?");
        getVillainStatement.setString(1, name);
        getVillainStatement.executeQuery();
        ResultSet villainResultSet = getVillainStatement.getResultSet();

        if (villainResultSet.next()) {
            this.isNewlyInserted = false;
            return villainResultSet.getInt("id");

        } else {
//            connection.setAutoCommit(false);
            PreparedStatement insertNewVillainStatement = connection.prepareStatement(
                    "INSERT INTO villains (name, evilness_factor) VALUE (?, 'evil')");
            insertNewVillainStatement.setString(1, name);
            insertNewVillainStatement.executeUpdate();

            Statement getIdFromDB = connection.createStatement();
            getIdFromDB.execute("SELECT LAST_INSERT_ID() AS id");
//            connection.commit();
//            connection.setAutoCommit(true);
            ResultSet villainIdResultSet = getIdFromDB.getResultSet();

//            if (!villainResultSet.next())
//                throw new SQLException("Unable to create villain " + name + " into DB");
            this.isNewlyInserted = true;
            villainIdResultSet.next();
            return villainIdResultSet.getInt("id");
        }
    }

    private void loadMinions() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT m.id, m.name, m.age, t.id as town_id, t.name as town, t.country " +
                        "FROM minions AS m " +
                        "LEFT JOIN minions_villains mv ON m.id = mv.minion_id " +
                        "LEFT JOIN towns AS t ON m.town_id = t.id " +
                        "WHERE mv.villain_id = ?");

        statement.setInt(1, this.id);
        statement.executeQuery();
        ResultSet set = statement.getResultSet();

        while (set.next()) {
            Town town = new Town(set.getInt("town_id"), set.getString("name"),
                    set.getString("country"), false, connection);

            minions.add(new Minion(set.getInt("id"), set.getString("name"),
                    set.getInt("age"), town));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Villain: ").append(this.name).append(System.lineSeparator());
        int minionNumber = 1;
        for (Minion minion : minions) {
            builder.append(minionNumber++).append(". ").append(minion).append(System.lineSeparator());
        }
        return builder.toString();
    }

    public void addMinion(Minion minion) throws SQLException {
        // not part of the problem description, but what should we do if the minion is already added to the villain ?

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO minions_villains (minion_id, villain_id) VALUE (?, ?)");
        statement.setInt(1, minion.getId());
        statement.setInt(2, this.id);
        statement.executeUpdate();

    }
}
