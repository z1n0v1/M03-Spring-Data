package entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "player_statistics")
public class PlayerStatistics implements Serializable {
    private Game game;
    private Player player;
    private Integer scoredGoals;
    private Integer playerAssists;
    private Integer playedMinutes;

    @ManyToOne
    @Id
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @ManyToOne
    @Id
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Column(name = "scored_goals")
    public Integer getScoredGoals() {
        return scoredGoals;
    }

    public void setScoredGoals(Integer scoredGoals) {
        this.scoredGoals = scoredGoals;
    }

    @Column(name = "player_assists")
    public Integer getPlayerAssists() {
        return playerAssists;
    }

    public void setPlayerAssists(Integer playerAssists) {
        this.playerAssists = playerAssists;
    }

    @Column(name = "played_minutes")
    public Integer getPlayedMinutes() {
        return playedMinutes;
    }

    public void setPlayedMinutes(Integer playedMinutes) {
        this.playedMinutes = playedMinutes;
    }
}
