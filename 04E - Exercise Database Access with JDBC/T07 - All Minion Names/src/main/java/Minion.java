import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Minion {
    private final int id;
    private final String name;
    private final int age;
    private final Town town;

    public Minion(int id, String name, int age, Town town) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.town = town;
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

    @Override
    public String toString() {
        return name + " " + age;
    }
}
