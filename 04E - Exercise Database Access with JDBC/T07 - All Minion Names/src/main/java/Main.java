import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Connection connection = ConnectionFactory.getConnection(reader);

        List<String> allMinionNames = Minion.getAllMinionNames(connection);

        for (int i = 0; i < allMinionNames.size() / 2; i++) {
            System.out.println(allMinionNames.get(i));
            System.out.println(allMinionNames.get(allMinionNames.size() - 1 - i));
        }
//        if(allMinionNames.size() % 2 == 1) // what happens when there are odd number of minions ? Not that...
//            System.out.println(allMinionNames.get(allMinionNames.size() - 1));
    }
}
