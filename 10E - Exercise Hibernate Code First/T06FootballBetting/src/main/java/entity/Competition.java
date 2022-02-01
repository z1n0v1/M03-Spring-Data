package entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "competitions")
public class Competition {
    private Long id;
    private String name;
    private CompetitionType type;
    private Set<Game> games;

    public Competition() {
        games = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JoinColumn(name = "competition_type")
    @ManyToOne
    public CompetitionType getType() {
        return type;
    }

    public void setType(CompetitionType type) {
        this.type = type;
    }

    @OneToMany(mappedBy = "competition")
    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
