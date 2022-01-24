import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            Connection connection = getConnection(reader);

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT concat_ws(' ', u.first_name, u.last_name) as 'full_name'," +
                            "count(ug.game_id) as 'count' " +
                            "FROM users as u " +
                            "         LEFT JOIN users_games ug on u.id = ug.user_id " +
                            "WHERE user_name = ? " +
                            "GROUP BY u.user_name");

            System.out.print("Username: ");
            String username = reader.readLine();
            statement.setString(1, username);

            ResultSet set = statement.executeQuery();

            if(!set.next()) {
                System.out.println("No such user exists");
            } else {
                System.out.printf("User: %s%n%s has played %d games", username,
                        set.getString("full_name"), set.getInt("count"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection getConnection(BufferedReader reader) throws IOException, SQLException {

        System.out.print("DB username (default:root): ");
        String username = reader.readLine().trim();
        username = username.equals("") ? "root" : username;

        System.out.print("DB password: ");
        String password = reader.readLine().trim();

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/diablo", username, password);
    }
}
