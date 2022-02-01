package entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "countries")
public class Country {
    private String id;
    private String name;
    private Continent continent;
    private Set<Town> towns;

    public Country() {
        towns = new HashSet<>();
    }

    @Id
    @Column(length = 3)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(id.length() != 3)
            throw new IllegalArgumentException("Country code should be 3 letters long");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne()
    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    @OneToMany(mappedBy = "country")
    public Set<Town> getTowns() {
        return towns;
    }

    public void setTowns(Set<Town> towns) {
        this.towns = towns;
    }
}
