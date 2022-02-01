package entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "positions")
public class Position {
    private String id;
    private String description;
    private Set<Player> players;

    public Position() {
        players =new HashSet<>();
    }

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "position_description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "position")
    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
