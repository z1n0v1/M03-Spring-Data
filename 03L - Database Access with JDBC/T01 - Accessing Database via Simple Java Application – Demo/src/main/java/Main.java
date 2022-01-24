import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Connection connection = getConnection(reader);

        PreparedStatement stmt = connection.prepareStatement
                ("select * from employees where salary > ?");

        System.out.print("Salary: ");
        String salary = reader.readLine();
        stmt.setDouble(1, Double.parseDouble(salary));

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.printf("%s %s%n", rs.getString("first_name"), rs.getString("last_name"));
        }

    }

    private static Connection getConnection(BufferedReader reader) throws SQLException, IOException {

        System.out.print("DB User (default:root):");
        String user = reader.readLine().trim();
        user = user.equals("") ? "root" : user;

        System.out.print("Password (default:empty):");
        String password = reader.readLine().trim();

        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/soft_uni", props);
    }
}
