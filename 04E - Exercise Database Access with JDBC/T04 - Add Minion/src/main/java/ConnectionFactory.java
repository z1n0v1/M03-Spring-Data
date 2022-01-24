import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection(BufferedReader reader) throws IOException, SQLException {

        System.out.print("DB User (default:root): ");
        String user = reader.readLine();
        user = user.equals("") ? "root" : user;

        System.out.print("DB Password: ");
        String password = reader.readLine();

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", user, password);
    }
}
