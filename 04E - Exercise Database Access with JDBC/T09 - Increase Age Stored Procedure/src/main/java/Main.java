import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Connection connection = ConnectionFactory.getConnection(reader);

        System.out.print("Minion ID: ");
        int minionID = Integer.parseInt(reader.readLine());

        // The procedure creation SQL if in /src/main/db/procedure.sql
        CallableStatement callableStatement = connection.prepareCall(
                "{call usp_get_older(?)}");
        callableStatement.setInt(1, minionID);
        callableStatement.execute();

        Minion minion = new Minion(minionID, connection);
        System.out.println(minion);

    }
}
