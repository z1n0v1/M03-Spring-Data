package entity;

import javax.persistence.*;

@Entity
@Table(name = "trucks")
public class Truck extends Vehicle{

    @Id
//    Second diagram, 2. Relationships
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double loadCapacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(Double loadCapacity) {
        this.loadCapacity = loadCapacity;
    }
}
