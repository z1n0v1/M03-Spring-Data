import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Villain {
    private final int id;
    private final String name;
    private final Connection connection;
    private final List<Minion> minions;

    public Villain(int id, Connection connection) throws SQLException {
        this.id = id;
        this.connection = connection;

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

    private void loadMinions() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT m.name, m.age " +
                        "FROM minions AS m " +
                        "         LEFT JOIN minions_villains mv ON m.id = mv.minion_id " +
                        "WHERE mv.villain_id = ?");
        statement.setInt(1, this.id);
        statement.executeQuery();
        ResultSet set = statement.getResultSet();

        while (set.next()) {
            minions.add(new Minion(
                    set.getString("name"),
                    set.getInt("age")));
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
}
