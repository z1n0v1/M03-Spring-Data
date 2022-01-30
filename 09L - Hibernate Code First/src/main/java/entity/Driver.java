package entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String full_name;

    // Only this makes sense. In the problem description is written that
    // Driver has ManyToMany relationship with Cars
    // On the diagram - the drivers has many-to-many relationship with trucks...
    // To get a good db, we should switch the inheritance strategy.

    // If we use the table per class strategy, we could connect cleanly the drivers to trucks or cars,
    // But I am not sure about the abstract class Vehicle.
    @ManyToMany
    private Set<Vehicle> vehicles;

    public Driver() {
        super();
        this.vehicles = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
