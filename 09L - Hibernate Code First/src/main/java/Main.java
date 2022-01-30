import entity.*;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManager entityManager = Persistence
                .createEntityManagerFactory("transportation")
                .createEntityManager();

        entityManager.getTransaction().begin();

        Driver driver = new Driver();
        driver.setFull_name("Pesho Petrov");
        entityManager.persist(driver);

        PlateNumber plateNumber = new PlateNumber();
        plateNumber.setNumber("ABCED");
        entityManager.persist(plateNumber);

        Car car = new Car();
        car.setPlateNumber(plateNumber);
        car.getDrivers().add(driver);
        entityManager.persist(car);

        Truck truck = new Truck();
        truck.setLoadCapacity(10.0);
        entityManager.persist(truck);

        Company company = new Company();
        company.setName("Acme LLC.");
        entityManager.persist(company);

        Plane plane = new Plane();
        plane.setPassengerCapacity(120);
        plane.setCompany(company);
        plane.getDrivers().add(driver);
        entityManager.persist(plane);

        entityManager.getTransaction().commit();




    }
}
