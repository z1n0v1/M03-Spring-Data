import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Connection connection = getConnection(reader);

        System.out.print("Villain id: ");
        int id = Integer.parseInt(reader.readLine());

        try {
            Villain villain = new Villain(id, connection);

            System.out.println(villain);

        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }


    }

    private static Connection getConnection(BufferedReader reader) throws IOException, SQLException {

        System.out.print("DB User (default:root): ");
        String user = reader.readLine();
        user = user.equals("") ? "root" : user;

        System.out.print("DB Password: ");
        String password = reader.readLine();

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", user, password);
    }
}
