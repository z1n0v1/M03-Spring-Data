import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Connection connection = connectToDB(reader);

        Statement statement = connection.createStatement();
        statement.execute(
                "SELECT v.name, COUNT(DISTINCT mv.minion_id) AS 'count' " +
                        "    FROM villains AS v " +
                        "    RIGHT JOIN minions_villains mv ON v.id = mv.villain_id " +
                        "    GROUP BY v.name " +
                        "    HAVING `count` > 15" +
                        "    ORDER BY `count` DESC"
        );
        ResultSet set = statement.getResultSet();

        while (set.next()) {
            System.out.printf("%s %d", set.getString("name"), set.getInt("count"));
        }
    }

    private static Connection connectToDB(BufferedReader reader) throws IOException, SQLException {

        System.out.print("DB User (default:root): ");
        String user = reader.readLine();
        user = user.equals("") ? "root" : user;

        System.out.print("DB Password: ");
        String password = reader.readLine();

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", user, password);
    }
}
