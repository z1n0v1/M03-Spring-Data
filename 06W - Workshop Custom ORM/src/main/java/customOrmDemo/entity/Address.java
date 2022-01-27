package customOrmDemo.entity;

import miniOrm.anotation.Column;
import miniOrm.anotation.Entity;
import miniOrm.anotation.Id;

@Entity(tableName = "addresses")
public class Address {

    @Id
    private long id;

    @Column(name = "street", columnDefinition = "VARCHAR(255)")
    private String street;

    @Column(name = "street_number", columnDefinition = "VARCHAR(255)")
    private String streetNumber;

    @Column(name = "people_count", columnDefinition = "INT(11)")
    private int peopleCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }
}
