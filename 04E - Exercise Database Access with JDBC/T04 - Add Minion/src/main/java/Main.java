import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Connection connection = ConnectionFactory.getConnection(reader);

        String[] tokens = reader.readLine().split("\\s+");
        String minionName = tokens[1];
        int minionAge = Integer.parseInt(tokens[2]);
        String minionTownName = tokens[3];

        tokens = reader.readLine().split("\\s+");
        String villainName = tokens[1];

        Town minionTown = new Town(minionTownName, connection);
        if(minionTown.isNewlyInserted()) {
            System.out.printf("Town %s was added to the database.%n", minionTownName);
        }

        Villain villain = new Villain(villainName, connection);
        if (villain.isNewlyInserted()) {
            System.out.printf("Villain %s was added to the database.%n", villainName);
        }

        Minion minion = Minion.createMinionInDB(minionName, minionAge, minionTown, connection);

        villain.addMinion(minion);
        System.out.printf("Successfully added %s to be minion of %s", minionName, villainName);








    }
}
