package entity;

import javax.persistence.*;

@Entity
@Table(name = "planes")
public class Plane extends Vehicle {

    @Id
//    Second diagram, 2. Relationships
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer passengerCapacity;

    @ManyToOne
    private Company company;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(Integer passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
