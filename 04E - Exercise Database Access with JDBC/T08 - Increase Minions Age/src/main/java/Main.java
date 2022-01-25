import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Connection connection = ConnectionFactory.getConnection(reader);

        System.out.print("Minion IDs: ");
        int[] minionIds = Arrays.stream(reader.readLine().split("\\s+"))
                .mapToInt(Integer::parseInt).toArray();

        for (int id : minionIds) {
            Minion minion = new Minion(id, connection);
            minion.updateName(minion.getName().toLowerCase());
            minion.updateAge(minion.getAge() + 1);
        }

        for (Minion minion : Minion.getAllMinions(connection)) {
            System.out.println(minion);
        }

//        List<Minion> minions = Minion.getAllMinions(connection);



    }
}
