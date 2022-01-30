package entity;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class Car extends Vehicle {
    @Id
//    Second diagram, 2. Relationships
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer seats;

    @OneToOne
    private PlateNumber plateNumber;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public PlateNumber getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(PlateNumber plateNumber) {
        this.plateNumber = plateNumber;
    }
}
