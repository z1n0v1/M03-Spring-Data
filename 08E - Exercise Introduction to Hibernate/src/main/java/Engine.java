import exercise.T02ChangeCasing;
import exercise.T03ContainsEmployee;
import exercise.T04EmployeesWithSalaryOver;
import exercise.T05EmployeesFromDepartment;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Engine implements Runnable {
    private final EntityManager entityManager;
    private final BufferedReader reader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {

        try {
            System.out.println(" 2) Change casing");
            System.out.println(" 3) Contains Employee");
            System.out.println(" 4) Employees with Salary Over 50 000");
            System.out.println(" 5) Employees from Department");

            System.out.print("Exercise number: ");
            int exerciseNumber = Integer.parseInt(reader.readLine());

            switch (exerciseNumber) {
                case 2: {
                    T02ChangeCasing t02ChangeCasing =
                            new T02ChangeCasing(entityManager);
                    t02ChangeCasing.run();
                    break;
                }
                case 3: {
                    T03ContainsEmployee t03ContainsEmployee =
                            new T03ContainsEmployee(entityManager, reader);
                    t03ContainsEmployee.run();
                    break;
                }
                case 4: {
                    T04EmployeesWithSalaryOver t04EmployeesWithSalaryOver =
                            new T04EmployeesWithSalaryOver(entityManager);
                    t04EmployeesWithSalaryOver.run();
                    break;
                }
                case 5: {
                    T05EmployeesFromDepartment t05EmployeesFromDepartment =
                            new T05EmployeesFromDepartment(entityManager);
                    t05EmployeesFromDepartment.run();
                    break;
                }
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }
}
