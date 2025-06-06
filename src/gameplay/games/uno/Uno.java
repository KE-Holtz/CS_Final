package gameplay.games.uno;
import java.util.ArrayList;

import backend.*;
import gameplay.*;
import gameplay.games.*;
import backend.globalvars.*;
import backend.publicvars.*;

public class Uno extends Game{
    private Lobby lobby;
    private Player self;
    private GlobalVar<Card> topCard;
    private PublicInt handSize;
    private ArrayList<Card> hand;

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
        UnoWindow window = new UnoWindow();
        window.updateTopCard(topCard.getValue().orElse(Card.randomNonWild()));
        hand = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            hand.add(Card.random());
        }
        handSize.setValue(hand.size());
    }

    @Override
    public boolean periodic() {
        handSize.setValue(hand.size());
        return true;
    }

    @Override
    public void endGame() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endGame'");
    }

}
