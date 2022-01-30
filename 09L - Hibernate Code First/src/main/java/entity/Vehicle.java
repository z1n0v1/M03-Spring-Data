package entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicle")
// 1. Vehicle Hierarchy
// First diagram
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

// Second diagram
//@Inheritance(strategy = InheritanceType.JOINED)

// Third diagram
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

// 2. Relationships
// Based on TABLE_PER_CLASS inheritance strategy.
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

// 2. Relationships, Drivers to Vehicles
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class Vehicle {
    @Id
    // Second diagram, last problem
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String model;

    private BigDecimal price;

    private String fuelType;

    // Only this makes sense. In the problem description is written that
    // Driver has ManyToMany relationship with Cars
    // On the diagram - the drivers has many-to-many relationship with trucks..
    // To get a good db, we should switch the inheritance strategy to JOIN.
    @ManyToMany(mappedBy = "vehicles")
    private Set<Driver> drivers;

    public Vehicle() {
        drivers = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }
}
