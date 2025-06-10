package gameplay.games.uno;
import java.util.ArrayList;
import java.util.Comparator;

import backend.*;
import gameplay.*;
import gameplay.games.*;
import backend.globalvars.*;
import backend.publicvars.*;

enum State{
    WAITING,
    TURN,
}

public class Uno extends Game{
    private Lobby lobby;
    private Player self;

    private GlobalVar<Card> topCard;
    private PublicInt handSize;
    private ArrayList<Card> hand;
    private UnoWindow uwu;

    private State state;

    public Uno() {
        setName("Uno");
    }

    @Override
    public void initialize(Session session) {
        lobby = session.getLobby();
        self = lobby.getClientPlayer();
        topCard = new GlobalVar<Card>(session, "card", Card::fromString, Card.random());
        handSize = new PublicInt(self, "handSize");
        self.addVariable(handSize);
    }

    @Override
    public void startGame() {
        uwu = new UnoWindow();
        uwu.updateTopCard(topCard.getValue().orElse(Card.randomNonWild()));
        hand = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            hand.add(Card.random());
        }
        handSize.setValue(hand.size());
        lobby.getPlayers().sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
        uwu.updateHand(hand);
    }

    @Override
    public boolean periodic() {
        switch (state) {
            case WAITING:

                break;

            default:
                break;
        }
        handSize.setValue(hand.size());
        uwu.reDraw();
        return true;
    }

    @Override
    public void endGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

}
