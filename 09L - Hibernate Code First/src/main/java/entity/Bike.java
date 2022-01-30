package entity;

import javax.persistence.*;

@Entity
@Table(name = "bikes")
public class Bike extends Vehicle {

    @Id
//    Second diagram, 2. Relationships
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}

