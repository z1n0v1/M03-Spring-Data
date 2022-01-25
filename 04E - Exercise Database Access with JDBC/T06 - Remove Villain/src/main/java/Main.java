import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Connection connection = ConnectionFactory.getConnection(reader);

        System.out.print("Villian ID: ");
        int id = Integer.parseInt(reader.readLine());

        try {
            Villain villain = new Villain(id, connection);
            // and this operation will invalidate the villain
            int numReleasedMinions = Villain.removeVillain(id, connection);

            System.out.printf("%s was deleted%n", villain.getName());
            System.out.printf("%d minions released%n", numReleasedMinions);

        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
