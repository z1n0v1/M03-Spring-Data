import exercise.*;

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
            System.out.println(" 6) Adding a New Address and Updating Employee");
            System.out.println(" 7) Addresses with Employee Count");
            System.out.println(" 8) Get Employee with Project");
            System.out.println(" 9) Find Latest 10 Projects");
            System.out.println("10) Increase Salaries");
            System.out.println("11) Find Employees by First Name");
            System.out.println("12) Employees Maximum Salaries");
            System.out.println("13) Remove Towns");

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
                case 6: {
                    T06NewAddressUpdatingEmployee t06NewAddressUpdatingEmployee =
                            new T06NewAddressUpdatingEmployee(entityManager, reader);
                    t06NewAddressUpdatingEmployee.run();
                    break;
                }
                case 7: {
                    T07AddressesEmployeeCount t07AddressesEmployeeCount =
                            new T07AddressesEmployeeCount(entityManager);
                    t07AddressesEmployeeCount.run();
                    break;
                }
                case 8: {
                    T08GetEmployeeWithProject t08GetEmployeeWithProject =
                            new T08GetEmployeeWithProject(entityManager, reader);
                    t08GetEmployeeWithProject.run();
                    break;
                }
                case 9: {
                    T09FindLatestProjects t09FindLatestProjects =
                            new T09FindLatestProjects(entityManager);
                    t09FindLatestProjects.run();
                    break;
                }
                case 10: {
                    T10IncreaseSalaries t10IncreaseSalaries =
                            new T10IncreaseSalaries(entityManager);
                    t10IncreaseSalaries.run();
                    break;
                }
                case 11: {
                    T11FindEmployeesByFirstName t11FindEmployeesByFirstName =
                            new T11FindEmployeesByFirstName(entityManager, reader);
                    t11FindEmployeesByFirstName.run();
                    break;
                }
                case 12: {
                    T12EmployeesMaximumSalaries t12EmployeesMaximumSalaries =
                            new T12EmployeesMaximumSalaries(entityManager);
                    t12EmployeesMaximumSalaries.run();
                    break;
                }
                case 13: {
                    T13RemoveTowns t13RemoveTowns =
                            new T13RemoveTowns(entityManager, reader);
                    t13RemoveTowns.run();
                }
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }
}
