package entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "bet_games")
public class BetGame implements Serializable {
    private Game game;
    private Bet bet;
    private ResultPrediction resultPrediction;


    @JoinColumn(name = "game_id")
    @ManyToOne @Id
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @JoinColumn(name = "bet_id")
    @ManyToOne @Id
    public Bet getBet() {
        return bet;
    }

    public void setBet(Bet bet) {
        this.bet = bet;
    }

    @OneToOne
    @JoinColumn(name = "result_prediction", nullable = false)
    public ResultPrediction getResultPrediction() {
        return resultPrediction;
    }

    public void setResultPrediction(ResultPrediction resultPrediction) {
        this.resultPrediction = resultPrediction;
    }
}
