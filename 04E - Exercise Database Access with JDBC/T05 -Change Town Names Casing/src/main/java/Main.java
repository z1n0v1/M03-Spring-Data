import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            Connection connection = ConnectionFactory.getConnection(reader);

            System.out.print("Country: ");
            String country = reader.readLine();
            List<Town> towns = Town.getTownsByCountry(country, connection);

            if (!towns.isEmpty()){
                System.out.printf("%d town names were affected.%n[", towns.size());
                for (Town town : towns) {
                    town.updateName(town.getName().toUpperCase());
                }
                System.out.print(towns.stream().map(Town::getName).collect(Collectors.joining(", ")));
                System.out.println(']');
            } else {
                System.out.println("No town names were affected.");
            }

        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }

    }
}
