import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Minion {
    private final int id;
    private final String name;
    private final int age;
    private final Town town;
    private Connection connection;

    public Minion(int id, String name, int age, Town town) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.town = town;
    }

    public Minion(int id, Connection connection) throws SQLException {
        this.connection = connection;

        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM minions WHERE id = ?");
        statement.setInt(1, id);
        statement.executeQuery();
        ResultSet set = statement.getResultSet();
        if (!set.next())
            throw new IllegalArgumentException("There is no minion with id " + id);
        this.id = id;
        this.name = set.getString("name");
        this.age = set.getInt("age");
        this.town = new Town(set.getInt("town_id"), connection);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // Should this be in here ? We don't reuse any other code from Minion... I should refactor it by the end of the
    // exercise. Maybe it will pay back if I need to gather all solutions of the exercises in one project as one
    // should be able to upload only homework with one pom.xml file and src folder... We'll see how that goes.
    public static List<String> getAllMinionNames(Connection connection) throws SQLException {
        List<String> minionNames = new ArrayList<>();

        Statement statement = connection.createStatement();
        statement.execute("SELECT name FROM minions ORDER BY id");
        ResultSet set = statement.getResultSet();
        while (set.next()) {
            minionNames.add(set.getString(1));
        }
        return minionNames;
    }

    public static List<Minion> getAllMinions(Connection connection) throws SQLException {
        List<Minion> minions = new ArrayList<>();

        Statement statement = connection.createStatement();
        statement.execute("SELECT id FROM minions ORDER BY id");
        ResultSet set = statement.getResultSet();
        while (set.next()) {
            minions.add(new Minion(set.getInt("id"), connection));
        }
        return minions;
    }

    public int getId() {
        return id;
    }

    public static Minion createMinionInDB(String name, int age, Town town, Connection connection) throws SQLException {

//        connection.setAutoCommit(false); // Start transaction
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO minions (name, age, town_id) VALUE (?, ?, ?)");
        statement.setString(1, name);
        statement.setInt(2, age);
        statement.setInt(3, town.getId());
        statement.executeUpdate();

        Statement idFromDBStatement = connection.createStatement();
        idFromDBStatement.execute("SELECT last_insert_id() as id");
//        connection.commit();
//        connection.setAutoCommit(true); // End transaction

        ResultSet resultSet = idFromDBStatement.getResultSet();
        resultSet.next();
        int minionId = resultSet.getInt("id");

        return new Minion(minionId, name, age, town);
    }

    public void updateName(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE minions SET name = ? WHERE id = ?");
        statement.setString(1, name);
        statement.setInt(2, this.id);
        if(statement.executeUpdate() != 1)
            throw new IllegalArgumentException(
                    "Cannot change the name of minion with id "
                            + this.id + " to " + name);
    }

    public void updateAge(int age) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE minions SET age = ? WHERE id = ?");
        statement.setInt(1, age);
        statement.setInt(2, this.id);
        if(statement.executeUpdate() != 1)
            throw new IllegalArgumentException(
                    "Cannot change the age of minion with id "
                            + this.id + " to " + age);
    }

    @Override
    public String toString() {
        return name + " " + age;
    }
}
