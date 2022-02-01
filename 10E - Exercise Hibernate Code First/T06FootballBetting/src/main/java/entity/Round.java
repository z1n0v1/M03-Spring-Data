package entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "rounds")
public class Round {
    private Long id;
    private String name;
    private Set<Game> games;

    public Round() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "round")
    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
