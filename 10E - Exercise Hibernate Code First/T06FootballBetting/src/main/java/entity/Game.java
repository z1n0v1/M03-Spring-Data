package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "games")
public class Game implements Serializable {
    private Long id;
    private Team homeTeam;
    private Team awayTeam;
    private Integer homeGoals;
    private Integer awayGoals;
    private LocalDateTime dateTime;
    private Double homeTeamWinBetRate;
    private Double awayTeamWinBetRate;
    private Double drawGameBetRate;
    private Round round;
    private Competition competition;
    private Set<Player> players;
    private Set<Bet> bets;

    public Game() {
        players = new HashSet<>();
        bets = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "home_team")
    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    @ManyToOne
    @JoinColumn(name = "away_team")
    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    @Column(name = "home_team_goals")
    public Integer getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(Integer homeGoals) {
        this.homeGoals = homeGoals;
    }
    @Column(name = "away_team_goals")
    public Integer getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(Integer awayGoals) {
        this.awayGoals = awayGoals;
    }

    @Column(name = "date_time")
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "home_team_win_bet_rate")
    public Double getHomeTeamWinBetRate() {
        return homeTeamWinBetRate;
    }

    public void setHomeTeamWinBetRate(Double homeTeamWinBetRate) {
        this.homeTeamWinBetRate = homeTeamWinBetRate;
    }
    @Column(name = "away_team_win_bet_rate")
    public Double getAwayTeamWinBetRate() {
        return awayTeamWinBetRate;
    }

    public void setAwayTeamWinBetRate(Double awayTeamWinBetRate) {
        this.awayTeamWinBetRate = awayTeamWinBetRate;
    }
    @Column(name = "draw_game_bet_rate")
    public Double getDrawGameBetRate() {
        return drawGameBetRate;
    }

    public void setDrawGameBetRate(Double drawGameBetRate) {
        this.drawGameBetRate = drawGameBetRate;
    }

    @ManyToOne
    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    @ManyToOne
    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    @ManyToMany(mappedBy = "games")
    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @ManyToMany
    public Set<Bet> getBets() {
        return bets;
    }

    public void setBets(Set<Bet> bets) {
        this.bets = bets;
    }
}
