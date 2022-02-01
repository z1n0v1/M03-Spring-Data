package entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "players")
public class Player {
    private Long id;
    private String name;
    private Short squadNumber;
    private Team team;
    private Position position;
    private Boolean isInjured;
    private Set<Game> games;
    private Set<PlayerStatistics> statistics;

    public Player() {
        games = new HashSet<>();
        statistics = new HashSet<>();
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

    @Column(name = "squad_number")
    public Short getSquadNumber() {
        return squadNumber;
    }

    public void setSquadNumber(Short squadNumber) {
        this.squadNumber = squadNumber;
    }

    @ManyToOne
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @ManyToOne
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Column(name = "is_injured")
    public Boolean getInjured() {
        return isInjured;
    }

    public void setInjured(Boolean injured) {
        isInjured = injured;
    }

    @ManyToMany
    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    @OneToMany(mappedBy = "player")
    public Set<PlayerStatistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(Set<PlayerStatistics> statistics) {
        this.statistics = statistics;
    }
}
